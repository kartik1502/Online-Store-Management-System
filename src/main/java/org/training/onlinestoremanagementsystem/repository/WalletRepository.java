package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.entity.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, String> {

    Optional<Wallet> findWalletByWalletIdAndUser(String walletId, User user);

    Optional<Wallet> findWalletByWalletType(String walletType);

    List<Wallet> findWalletByWalletTypeInAndUser(List<String> walletType, User user);

    Optional<Wallet> findWalletByWalletTypeAndUser(String walletType, User user);

    List<Wallet> findAllByWalletTypeIn(List<String> walletTypes);

    List<Wallet> findAllByWalletIdIn(List<String> walletIds);
}
