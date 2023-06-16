package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.entity.*;
import org.training.onlinestoremanagementsystem.exception.InSufficientWalletBalance;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.exception.NoSuchWalletExists;
import org.training.onlinestoremanagementsystem.exception.QuantityExceeded;
import org.training.onlinestoremanagementsystem.repository.*;
import org.training.onlinestoremanagementsystem.service.PaymentService;

import java.util.Map;
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
                () -> new NoSuchWalletExists("Cart with status " + CART_ORDER_PENDING + " does not exist")
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
        wallet.setWalletBalance(wallet.getWalletBalance() - cart.getTotalPrice());
        cart.setStatus(CART_ORDER_COMPLETED);
        cartRepository.save(cart);
        walletRepository.save(wallet);
        paymentRepository.save(payment);
        return new ResponseDto("200","Payment successful");
    }
}
