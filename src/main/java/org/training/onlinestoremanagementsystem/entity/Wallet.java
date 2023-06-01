package org.training.onlinestoremanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    private String walletId;

    private String walletType;

    private double walletBalance;

    @ManyToOne
    private User user;
}