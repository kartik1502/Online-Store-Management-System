package org.training.onlinestoremanagementsystem.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.AddToCartDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.ViewCartDto;
import org.training.onlinestoremanagementsystem.entity.*;
import org.training.onlinestoremanagementsystem.exception.NoSuchCartExists;
import org.training.onlinestoremanagementsystem.exception.NoSuchProductExists;
import org.training.onlinestoremanagementsystem.exception.QuantityExceeded;
import org.training.onlinestoremanagementsystem.repository.CartRepository;
import org.training.onlinestoremanagementsystem.repository.ProductQuantityRepository;
import org.training.onlinestoremanagementsystem.repository.ProductRepository;
import org.training.onlinestoremanagementsystem.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductQuantityRepository productQuantityRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Test
    void testAddToCart_CartPresent_ProductsNotRepeated() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1, 5));
        addToCartDtos.add(new AddToCartDto(2, 3));
        addToCartDtos.add(new AddToCartDto(3, 6));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(4);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);


        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(5);
        products.add(product);
        product = new Product();
        product.setProductId(3);
        product.setQuantity(7);
        products.add(product);

        List<Integer> productIds = List.of(1, 2, 3);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);

        ResponseDto responseDto = cartService.addToCart(authToken, addToCartDtos);
        assertEquals("Successfully added to cart", responseDto.getResponseMessage());
    }

    @Test
    void testAddToCart_CartPresent_ProductsRepeated() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1, 5));
        addToCartDtos.add(new AddToCartDto(2, 3));
        addToCartDtos.add(new AddToCartDto(3, 6));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(1);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);

        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(5);
        products.add(product);
        product = new Product();
        product.setProductId(3);
        product.setQuantity(7);
        products.add(product);

        List<Integer> productIds = List.of(1, 2, 3);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);

        ResponseDto responseDto = cartService.addToCart(authToken, addToCartDtos);
        assertEquals("Successfully added to cart", responseDto.getResponseMessage());
    }

    @Test
    void testAddToCart_CartPresent_ProductsNotPresent() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1, 5));
        addToCartDtos.add(new AddToCartDto(2, 3));
        addToCartDtos.add(new AddToCartDto(3, 6));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(1);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);

        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        products.add(product);

        List<Integer> productIds = List.of(1, 2, 3);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);
        assertThrows(NoSuchProductExists.class, () -> cartService.addToCart(authToken, addToCartDtos));
    }

    @Test
    void testAddToCart_CartPresent_ProductQuantityInSufficient() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1, 8));
        addToCartDtos.add(new AddToCartDto(2, 15));
        addToCartDtos.add(new AddToCartDto(3, 12));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(1);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);

        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(5);
        products.add(product);
        product = new Product();
        product.setProductId(3);
        product.setQuantity(7);
        products.add(product);

        List<Integer> productIds = List.of(1, 2, 3);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);
        assertThrows(QuantityExceeded.class, () -> cartService.addToCart(authToken, addToCartDtos));
    }

    @Test
    void testAddToCart_CartNotPresent() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1, 5));
        addToCartDtos.add(new AddToCartDto(2, 3));
        addToCartDtos.add(new AddToCartDto(3, 6));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.empty());
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.empty());

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        products.add(product);
        product = new Product();
        product.setProductId(2);
        product.setQuantity(5);
        products.add(product);
        product = new Product();
        product.setProductId(3);
        product.setQuantity(7);
        products.add(product);

        List<Integer> productIds = List.of(1, 2, 3);
        Mockito.when(productRepository.findAllByProductIdIn(productIds)).thenReturn(products);

        ResponseDto responseDto = cartService.addToCart(authToken, addToCartDtos);
        assertEquals("Successfully added to cart", responseDto.getResponseMessage());
    }

    @Test
    void testViewCart_CartNotPresent() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.empty());

        assertThrows(NoSuchCartExists.class, () -> cartService.viewCart(authToken));
    }

    @Test
    void testViewCart_CartPresent_OneProduct() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);
        cart.setStatus("Cart Order Pending");

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByStatusAndUser("Cart Order Pending",user)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(1);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);

        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        product.setPrice(100);
        products.add(product);

        Mockito.when(productRepository.findAllByProductIdIn(List.of(1))).thenReturn(products);

        ViewCartDto viewCartDto = cartService.viewCart(authToken);
        assertNotNull(viewCartDto);
        assertEquals(300, viewCartDto.getTotalPrice());
        assertEquals(1, viewCartDto.getProductQuantityDtos().size());
    }

    @Test
    void testUpdateCart_CartNotPresent() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.empty());

        assertThrows(NoSuchCartExists.class, () -> cartService.updateCart(authToken, new ArrayList<>()));
    }

    @Test
    void testUpdateCart_CartPresent_OnlyProductIdGiven() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(4);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setProductId(1);
        productQuantity.setQuantity(3);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setProductId(2);
        productQuantity.setQuantity(6);
        productQuantities.add(productQuantity);

        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        product.setPrice(100);
        products.add(product);

        Mockito.when(productRepository.findAllByProductIdIn(List.of(1))).thenReturn(products);
        Mockito.when(productRepository.saveAll(products)).thenReturn(products);

        ResponseDto responseDto = cartService.updateCart(authToken, addToCartDtos);
        assertNotNull(responseDto);
        assertEquals("Successfully updated cart", responseDto.getResponseMessage());
    }

    @Test
    void testUpdateCart_CartPresent_ProductIdAndQuantityGiven() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1, 2));

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(300);

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(cartService.getCartFromToken(authToken)).thenReturn(Optional.of(cart));

        List<ProductQuantity> productQuantities = new ArrayList<>();
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(4);
        productQuantity.setQuantity(5);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setProductId(1);
        productQuantity.setQuantity(3);
        productQuantities.add(productQuantity);
        productQuantity = new ProductQuantity();
        productQuantity.setProductId(2);
        productQuantity.setQuantity(6);
        productQuantities.add(productQuantity);

        Mockito.when(productQuantityRepository.findAllByCart(cart)).thenReturn(productQuantities);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(7);
        product.setPrice(100);
        products.add(product);

        Mockito.when(productRepository.findAllByProductIdIn(List.of(1))).thenReturn(products);
        Mockito.when(productRepository.saveAll(products)).thenReturn(products);

        ResponseDto responseDto = cartService.updateCart(authToken, addToCartDtos);
        assertNotNull(responseDto);
        assertEquals("Successfully updated cart", responseDto.getResponseMessage());
    }

    @Test
    void testDeleteCart_CartNotPresent() {

        String authToken = "gskgfklsrjty8qr76934hrkfsdjgh397054893ihgjklhsdfjklgjkas";
        String username = "kartikkulkarni1411@gmail.com";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("ka3k@1411");
        user.setContactNumber("6361921186");

        Mockito.when(jwtTokenUtil.getUsernameFromToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findCartByUser(user)).thenReturn(Optional.empty());

        assertThrows(NoSuchCartExists.class, () -> cartService.deleteCart(authToken));
    }
}
