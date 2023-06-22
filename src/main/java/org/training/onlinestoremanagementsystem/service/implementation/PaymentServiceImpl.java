package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.AddToCartDto;
import org.training.onlinestoremanagementsystem.dto.CartQuantity;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.TransactionDto;
import org.training.onlinestoremanagementsystem.entity.*;
import org.training.onlinestoremanagementsystem.exception.*;
import org.training.onlinestoremanagementsystem.repository.*;
import org.training.onlinestoremanagementsystem.service.PaymentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.training.onlinestoremanagementsystem.dto.Constants.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public String getUsernameFromAuthToken(String authToken) {
        return jwtTokenUtil.getUsernameFromToken(authToken.replace(TOKEN_PREFIX, ""));
    }

    @Override
    public ResponseDto makePayment(String authToken, String walletId) {

        User user = userRepository.findUserByEmailId(getUsernameFromAuthToken(authToken)).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + getUsernameFromAuthToken(authToken) + " does not exist")
        );
        Wallet wallet = walletRepository.findWalletByWalletIdAndUser(walletId, user).orElseThrow(
                () -> new NoSuchWalletExists("Wallet with wallet id " + walletId + " does not exist")
        );
        Cart cart = cartRepository.findCartByStatusAndUser(CART_ORDER_PENDING, user).orElseThrow(
                () -> new NoSuchCartExists("Cart with status " + CART_ORDER_PENDING + " does not exist")
        );
        Map<Integer, ProductQuantity> productQuantityMap = productQuantityRepository.findAllByCart(cart).stream().collect(Collectors.toMap(ProductQuantity::getProductId, Function.identity()));
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productQuantityMap.keySet().stream().collect(Collectors.toList())).stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        productQuantityRepository.findAllByCart(cart).forEach(productQuantity -> {
             Product product = productMap.get(productQuantity.getProductId());
             if(product.getQuantity() < productQuantity.getQuantity()){
                 throw new QuantityExceeded("Product with product id " + product.getProductId() + " has " + product.getQuantity() + " in stock");
             }
        });
        if(wallet.getWalletBalance() < cart.getTotalPrice()){
            throw new InSufficientWalletBalance("Insufficient wallet balance");
        }
        Payment payment = new Payment();
        payment.setPaymentType(wallet.getWalletType());
        payment.setCart(cart);
        payment.setWalletId(walletId);
        wallet.setWalletBalance(wallet.getWalletBalance() - cart.getTotalPrice());
        cart.setStatus(CART_ORDER_COMPLETED);
        cartRepository.save(cart);
        walletRepository.save(wallet);
        paymentRepository.save(payment);
        return new ResponseDto("200","Payment successful");
    }

    @Override
    public List<TransactionDto> getTransactions(String authToken) {

        User user = userRepository.findUserByEmailId(getUsernameFromAuthToken(authToken)).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + getUsernameFromAuthToken(authToken) + " does not exist"));

        List<TransactionDto> transactionDtos = new ArrayList<>();
        List<Cart> carts = cartRepository.findAllByUser(user);
        List<Payment> payments = paymentRepository.findAllByCartIn(carts);
        Map<Cart, Payment> paymentMap = payments.stream().collect(Collectors.toMap(Payment::getCart, Function.identity()));
        Map<Cart, List<ProductQuantity>> productQuantityMap = productQuantityRepository.findAllByCartIn(carts).stream().collect(Collectors.groupingBy(ProductQuantity::getCart));
        List<Integer> productIds = productQuantityMap.values().stream().flatMap(List::stream).map(ProductQuantity::getProductId).collect(Collectors.toList());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productIds).stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        carts.forEach(cart -> {
            Payment payment = paymentMap.get(cart);
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setPaymentDate(payment.getPaymentDate());
            transactionDto.setPaymentStatus(cart.getStatus());
            transactionDto.setPaymentType(payment.getPaymentType());
            transactionDto.setTotalPrice(cart.getTotalPrice());
            transactionDto.setFirstName(user.getFirstName());
            transactionDto.setLastName(user.getLastName());
            List<CartQuantity> cartQuantities = new ArrayList<>();
            productQuantityMap.get(cart).forEach(productQuantity -> {
                Product product = productMap.get(productQuantity.getProductId());
                cartQuantities.add(new CartQuantity(productQuantity.getProductId(), productQuantity.getQuantity(), product.getPrice(), productQuantity.getQuantity()*product.getPrice()));
            });
            transactionDto.setCartQuantities(cartQuantities);
            transactionDtos.add(transactionDto);
        });
        return transactionDtos;
    }
}