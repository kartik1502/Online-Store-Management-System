package org.training.onlinestoremanagementsystem.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.CartQuantity;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.TransactionDto;
import org.training.onlinestoremanagementsystem.entity.*;
import org.training.onlinestoremanagementsystem.exception.*;
import org.training.onlinestoremanagementsystem.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductQuantityRepository productQuantityRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void makePayment_UserNoTPresent() {

        String authToken = "kjhfdkruiotyio4rt89.eqrkhqweuiory24e;wruyqweiuory8q4.erwoiruhqwiuoh423";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExists.class, () -> paymentService.makePayment(authToken, "1"));
    }

    @Test
    void makePayment_WalletNoTPresent() {

        String authToken = "kjhfdkruiotyio4rt89.eqrkhqweuiory24e;wruyqweiuory8q4.erwoiruhqwiuoh423";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);

        User user = new User();
        user.setFirstName("Kartik");
        user.setEmailId("kartikkulkarni1411@gmail.com");
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));

        Mockito.when(walletRepository.findWalletByWalletIdAndUser("1", user)).thenReturn(Optional.empty());

        assertThrows(NoSuchWalletExists.class, () -> paymentService.makePayment(authToken, "1"));
    }

    @Test
    void makePayment_CartNoTPresent() {

        String authToken = "kjhfdkruiotyio4rt89.eqrkhqweuiory24e;wruyqweiuory8q4.erwoiruhqwiuoh423";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);

        User user = new User();
        user.setFirstName("Kartik");
        user.setEmailId("kartikkulkarni1411@gmail.com");
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet();
        wallet.setWalletBalance(1000);
        wallet.setWalletType("paytm");

        Mockito.when(walletRepository.findWalletByWalletIdAndUser("1", user)).thenReturn(Optional.of(wallet));
        Mockito.when(cartRepository.findCartByStatusAndUser("1", user)).thenReturn(Optional.empty());

        assertThrows(NoSuchCartExists.class, () -> paymentService.makePayment(authToken, "1"));
    }

    @Test
    void makePayment_SufficientProductQuantity() {

        String authToken = "kjhfdkruiotyio4rt89.eqrkhqweuiory24e;wruyqweiuory8q4.erwoiruhqwiuoh423";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);

        User user = new User();
        user.setFirstName("Kartik");
        user.setEmailId("kartikkulkarni1411@gmail.com");
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet();
        wallet.setWalletId("1");
        wallet.setWalletBalance(1000);
        wallet.setWalletType("paytm");
        Mockito.when(walletRepository.findWalletByWalletIdAndUser("1", user)).thenReturn(Optional.of(wallet));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("Cart Order Pending");
        cart.setTotalPrice(600.0);
        Mockito.when(cartRepository.findCartByStatusAndUser("Cart Order Pending", user)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setCart(cart);
        productQuantity.setProductId(1);
        productQuantity.setQuantity(6);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setCart(cart);
        productQuantity.setProductId(2);
        productQuantity.setQuantity(9);
        productQuantities.add(productQuantity);
        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(12);
        product.setPrice(56);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(9);
        product.setPrice(78);
        products.add(product);

        List<Integer> productIds = List.of(1, 2);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);

        ResponseDto responseDto = paymentService.makePayment(authToken, "1");
        assertNotNull(responseDto);
    }

    @Test
    void makePayment_InSufficientProductQuantity() {

        String authToken = "kjhfdkruiotyio4rt89.eqrkhqweuiory24e;wruyqweiuory8q4.erwoiruhqwiuoh423";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);

        User user = new User();
        user.setFirstName("Kartik");
        user.setEmailId("kartikkulkarni1411@gmail.com");
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet();
        wallet.setWalletId("1");
        wallet.setWalletBalance(1000);
        wallet.setWalletType("paytm");
        Mockito.when(walletRepository.findWalletByWalletIdAndUser("1", user)).thenReturn(Optional.of(wallet));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("Cart Order Pending");
        cart.setTotalPrice(600.0);
        Mockito.when(cartRepository.findCartByStatusAndUser("Cart Order Pending", user)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setCart(cart);
        productQuantity.setProductId(1);
        productQuantity.setQuantity(6);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setCart(cart);
        productQuantity.setProductId(2);
        productQuantity.setQuantity(9);
        productQuantities.add(productQuantity);
        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(2);
        product.setPrice(56);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(9);
        product.setPrice(78);
        products.add(product);

        List<Integer> productIds = List.of(1, 2);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);

        assertThrows(QuantityExceeded.class, () -> paymentService.makePayment(authToken, "1"));
    }

    @Test
    void makePayment_InSuffientBalanace() {

        String authToken = "kjhfdkruiotyio4rt89.eqrkhqweuiory24e;wruyqweiuory8q4.erwoiruhqwiuoh423";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);

        User user = new User();
        user.setFirstName("Kartik");
        user.setEmailId("kartikkulkarni1411@gmail.com");
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet();
        wallet.setWalletId("1");
        wallet.setWalletBalance(100);
        wallet.setWalletType("paytm");
        Mockito.when(walletRepository.findWalletByWalletIdAndUser("1", user)).thenReturn(Optional.of(wallet));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("Cart Order Pending");
        cart.setTotalPrice(600.0);
        Mockito.when(cartRepository.findCartByStatusAndUser("Cart Order Pending", user)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setCart(cart);
        productQuantity.setProductId(1);
        productQuantity.setQuantity(6);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setCart(cart);
        productQuantity.setProductId(2);
        productQuantity.setQuantity(9);
        productQuantities.add(productQuantity);
        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(12);
        product.setPrice(56);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(9);
        product.setPrice(78);
        products.add(product);

        List<Integer> productIds = List.of(1, 2);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);

        assertThrows(InSufficientWalletBalance.class, () -> paymentService.makePayment(authToken, "1"));
    }

    @Test
    void testGetTransaction_UserNotFound(){

        String authToken = "kfejhrw7895t34jkhrkljghjk.elrkfhweuiohftiourtqwyerui.wrqjheuirhweuityowiuertq";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(paymentService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExists.class, () -> paymentService.getTransactions(authToken));
    }
}
