package org.training.onlinestoremanagementsystem.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.entity.Wallet;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.repository.UserRepository;
import org.training.onlinestoremanagementsystem.repository.WalletRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class WalletServiceImplTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testAddWallet_UserNotPresent() {

        String authToken = "jkldhsfkjlhserto357892405ri2u4jieqfhsgasdfgjkl.sleritopqwr.wertuwyrouipt";
        String username = "kartikkulkarni1411@gmail.com";

        Mockito.when(walletService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExists.class, () -> walletService.addWallet(authToken, "PHONE_PAY", 100));
    }

    @Test
    void testAddWallet_newWallet_GPAY() {

        String authToken = "jkldhsfkjlhserto357892405ri2u4jieqfhsgasdfgjkl.sleritopqwr.wertuwyrouipt";
        String username = "kartikkulkarni1411@gmail.com";
        String walletType = "gpay";


        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@14111");
        user.setFirstName("Kartik");

        Mockito.when(walletService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(walletRepository.findWalletByWalletType(walletType)).thenReturn(Optional.empty());

        ResponseDto responseDto = walletService.addWallet(authToken, walletType, 1000);
        assertNotNull(responseDto);
        assertEquals("Wallet added successfully", responseDto.getResponseMessage());
    }

    @Test
    void testAddWallet_newWallet_PAYTM() {

        String authToken = "jkldhsfkjlhserto357892405ri2u4jieqfhsgasdfgjkl.sleritopqwr.wertuwyrouipt";
        String username = "kartikkulkarni1411@gmail.com";
        String walletType = "paytm";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@14111");
        user.setFirstName("Kartik");

        Mockito.when(walletService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(walletRepository.findWalletByWalletType(walletType)).thenReturn(Optional.empty());

        ResponseDto responseDto = walletService.addWallet(authToken, walletType, 1000);
        assertNotNull(responseDto);
        assertEquals("Wallet added successfully", responseDto.getResponseMessage());
    }

    @Test
    void testAddWallet_newWallet_PHONE_PAY() {

        String authToken = "jkldhsfkjlhserto357892405ri2u4jieqfhsgasdfgjkl.sleritopqwr.wertuwyrouipt";
        String username = "kartikkulkarni1411@gmail.com";
        String walletType = "phonepay";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@14111");
        user.setFirstName("Kartik");

        Mockito.when(walletService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));
        Mockito.when(walletRepository.findWalletByWalletType(walletType)).thenReturn(Optional.empty());

        ResponseDto responseDto = walletService.addWallet(authToken, walletType, 1000);
        assertNotNull(responseDto);
        assertEquals("Wallet added successfully", responseDto.getResponseMessage());
    }

    @Test
    void testAddWallet_oldWallet() {

        String authToken = "jkldhsfkjlhserto357892405ri2u4jieqfhsgasdfgjkl.sleritopqwr.wertuwyrouipt";
        String username = "kartikkulkarni1411@gmail.com";
        String walletType = "phonepay";

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@14111");
        user.setFirstName("Kartik");

        Mockito.when(walletService.getUsernameFromAuthToken(authToken)).thenReturn(username);
        Mockito.when(userRepository.findUserByEmailId(username)).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet();
        wallet.setWalletBalance(500);
        wallet.setWalletType("phonepay");
        wallet.setUser(user);
        Mockito.when(walletRepository.findWalletByWalletType(walletType)).thenReturn(Optional.of(wallet));

        ResponseDto responseDto = walletService.addWallet(authToken, walletType, 1000);
        assertNotNull(responseDto);
        assertEquals("Wallet added successfully", responseDto.getResponseMessage());
    }
}
