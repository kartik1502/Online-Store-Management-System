package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.entity.Wallet;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.repository.UserRepository;
import org.training.onlinestoremanagementsystem.repository.WalletRepository;
import org.training.onlinestoremanagementsystem.service.WalletService;

import java.util.Optional;

import static org.training.onlinestoremanagementsystem.dto.Constants.*;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String getUsernameFromAuthToken(String authToken) {
        return jwtTokenUtil.getUsernameFromToken(authToken.replace(TOKEN_PREFIX, ""));
    }

    @Override
    public ResponseDto addWallet(String authToken, String walletType, double walletBalance) {

        User user = userRepository.findUserByEmailId(getUsernameFromAuthToken(authToken)).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + getUsernameFromAuthToken(authToken) + " does not exist")
        );
        Optional<Wallet> wallet = walletRepository.findWalletByWalletTypeAndUser(walletType, user);
        Wallet newWallet;
        if(wallet.isPresent()){
            newWallet = wallet.get();
            newWallet.setWalletBalance(wallet.get().getWalletBalance() + walletBalance);
        }
        else{
            newWallet = new Wallet();
            switch (walletType) {
                case PAYTM:
                    newWallet.setWalletId(user.getContactNumber()+"@paytm");
                    break;
                case GPAY:
                    newWallet.setWalletId(user.getContactNumber()+"@okicici");
                    break;
                case PHONE_PAY:
                    newWallet.setWalletId(user.getContactNumber()+"@ybl");
            }
            newWallet.setWalletType(walletType);
            newWallet.setWalletBalance(walletBalance);
            newWallet.setUser(user);
        }
        walletRepository.save(newWallet);
        return new ResponseDto("200", "Wallet added successfully");
    }
}
