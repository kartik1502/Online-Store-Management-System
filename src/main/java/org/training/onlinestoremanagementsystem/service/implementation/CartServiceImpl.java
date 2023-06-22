package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.AddToCartDto;
import org.training.onlinestoremanagementsystem.dto.ProductQuantityDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.ViewCartDto;
import org.training.onlinestoremanagementsystem.entity.Cart;
import org.training.onlinestoremanagementsystem.entity.Product;
import org.training.onlinestoremanagementsystem.entity.ProductQuantity;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.exception.NoSuchCartExists;
import org.training.onlinestoremanagementsystem.exception.NoSuchProductExists;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.exception.QuantityExceeded;
import org.training.onlinestoremanagementsystem.repository.CartRepository;
import org.training.onlinestoremanagementsystem.repository.ProductQuantityRepository;
import org.training.onlinestoremanagementsystem.repository.ProductRepository;
import org.training.onlinestoremanagementsystem.repository.UserRepository;
import org.training.onlinestoremanagementsystem.service.CartService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.training.onlinestoremanagementsystem.dto.Constants.CART_ORDER_PENDING;
import static org.training.onlinestoremanagementsystem.dto.Constants.TOKEN_PREFIX;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    @Value("${spring.application.responseCode}")
    private String responseCode;

    public Optional<Cart> getCartFromToken(String authToken){
        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        User user = userRepository.findUserByEmailId(username).get();
        return cartRepository.findCartByStatusAndUser(CART_ORDER_PENDING, user);
    }

    @Override
    public ResponseDto addToCart(String authToken, List<AddToCartDto> addToCartDtos) {

        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<Cart> cart = getCartFromToken(authToken);
        User user = userRepository.findUserByEmailId(username).get();
        Cart newCart;
        Map<Integer, ProductQuantity> productQuantityMap = null;
        if (cart.isPresent()) {
            newCart = cart.get();
            productQuantityMap = productQuantityRepository.findAllByCart(newCart).stream().collect(Collectors.toMap(ProductQuantity::getProductId, Function.identity()));
        }
        else{
            newCart = new Cart();
            newCart.setUser(user);
            newCart.setStatus(CART_ORDER_PENDING);
        }
        List<ProductQuantity> productQuantities = new ArrayList<>();
        Map<Integer, Product> products = productRepository.findAllByProductIdIn(addToCartDtos.stream().map(AddToCartDto::getProductId).collect(Collectors.toList())).stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        List<Product> productList = new ArrayList<>();
        Map<Integer, ProductQuantity> finalProductQuantityMap = productQuantityMap;
        addToCartDtos.forEach(addToCartDto -> {
            Product product = products.get(addToCartDto.getProductId());
            if(Objects.isNull(product)){
                throw new NoSuchProductExists("Product with Product Id: " + addToCartDto.getProductId() + " does not exist");
            }
            if(product.getQuantity() <= addToCartDto.getQuantity()){
                throw new QuantityExceeded("Product with Product Id: " + addToCartDto.getProductId() + " has only " + product.getQuantity() + " left");
            }
            product.setQuantity(product.getQuantity() - addToCartDto.getQuantity());
            ProductQuantity productQuantity;
            if(cart.isPresent() && finalProductQuantityMap.containsKey(addToCartDto.getProductId())){
                productQuantity = finalProductQuantityMap.get(addToCartDto.getProductId());
                productQuantity.setQuantity(productQuantity.getQuantity() + addToCartDto.getQuantity());
            }
            else{
                productQuantity = new ProductQuantity();
                productQuantity.setProductId(addToCartDto.getProductId());
                productQuantity.setQuantity(addToCartDto.getQuantity());
            }
            newCart.setTotalPrice(newCart.getTotalPrice() + product.getPrice() * addToCartDto.getQuantity());
            productQuantity.setCart(newCart);
            productQuantities.add(productQuantity);
            productList.add(product);
        });
        productRepository.saveAll(productList);
        productQuantityRepository.saveAll(productQuantities);
        return new ResponseDto(responseCode, "Successfully added to cart");
    }

    @Override
    public ViewCartDto viewCart(String authToken) {

        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        User user = userRepository.findUserByEmailId(username).orElseThrow(
                () -> new NoSuchUserExists("User does not exist")
        );
        Optional<Cart> cart = cartRepository.findCartByStatusAndUser(CART_ORDER_PENDING,user);
        if(!cart.isPresent()){
            throw new NoSuchCartExists("There are no pending cart");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByCart(cart.get());
        List<Integer> productIds = productQuantities.stream().map(ProductQuantity::getProductId).collect(Collectors.toList());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productIds).stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));

        ViewCartDto viewCartDto = new ViewCartDto();
        viewCartDto.setFirstName(user.getFirstName());
        viewCartDto.setLastName(user.getLastName());
        viewCartDto.setContactNumber(user.getContactNumber());
        viewCartDto.setCartStatus(cart.get().getStatus());
        viewCartDto.setTotalPrice(cart.get().getTotalPrice());

        List<ProductQuantityDto> productQuantityDtos = new ArrayList<>();
        productQuantities.forEach(productQuantity -> {
            ProductQuantityDto productQuantityDto = new ProductQuantityDto();
            productQuantityDto.setProductId(productQuantity.getProductId());
            productQuantityDto.setQuantity(productQuantity.getQuantity());
            productQuantityDto.setPrice(productMap.get(productQuantity.getProductId()).getPrice());
            productQuantityDto.setTotalPrice(productQuantity.getQuantity() * productMap.get(productQuantity.getProductId()).getPrice());
            productQuantityDtos.add(productQuantityDto);
        });
        viewCartDto.setProductQuantityDtos(productQuantityDtos);
        return viewCartDto;
    }

    @Override
    public ResponseDto updateCart(String authToken, List<AddToCartDto> addToCartDtos) {

        Optional<Cart> cart = getCartFromToken(authToken);
        if(!cart.isPresent()){
            throw new NoSuchCartExists("There are no pending cart");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByCart(cart.get());
        Map<Integer, ProductQuantity> productQuantityMap = productQuantities.stream().collect(Collectors.toMap(ProductQuantity::getProductId, Function.identity()));

        List<Integer> productIds = addToCartDtos.stream().map(AddToCartDto::getProductId).collect(Collectors.toList());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productIds).stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));

        List<ProductQuantity> deletedProductQuantities = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        addToCartDtos.forEach(addToCartDto -> {
            ProductQuantity productQuantity = productQuantityMap.get(addToCartDto.getProductId());
            Product product = productMap.get(addToCartDto.getProductId());
            if(Objects.equals(addToCartDto.getQuantity(), 0)){
                product.setQuantity(product.getQuantity() + productQuantity.getQuantity());
                cart.get().setTotalPrice(cart.get().getTotalPrice() - product.getPrice() * productQuantity.getQuantity());
                deletedProductQuantities.add(productQuantity);
            }
            else{
                product.setQuantity(product.getQuantity() + productQuantity.getQuantity() - addToCartDto.getQuantity());
                cart.get().setTotalPrice(cart.get().getTotalPrice() - (productQuantity.getQuantity() * product.getPrice()) + (product.getPrice() * addToCartDto.getQuantity()));
                productQuantity.setQuantity(addToCartDto.getQuantity());
            }
            productList.add(product);
        });
        productQuantityRepository.deleteAll(deletedProductQuantities);
        productRepository.saveAll(productList);
        return new ResponseDto(responseCode, "Successfully updated cart");
    }

    @Override
    public ResponseDto deleteCart(String authToken) {

        Optional<Cart> cart = getCartFromToken(authToken);
        if(!cart.isPresent()){
            throw new NoSuchCartExists("There are no pending cart");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByCart(cart.get());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productQuantities.stream().map(ProductQuantity::getProductId).collect(Collectors.toList())).stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        productQuantities.forEach(productQuantity -> {
            Product product = productMap.get(productQuantity.getProductId());
            product.setQuantity(product.getQuantity() + productQuantity.getQuantity());
        });
        productQuantityRepository.deleteAll(productQuantities);
        cartRepository.delete(cart.get());
        productRepository.saveAll(productMap.values());
        return new ResponseDto(responseCode, "Successfully deleted all the products in the cart");
    }
}