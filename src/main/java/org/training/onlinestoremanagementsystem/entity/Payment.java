package org.training.onlinestoremanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID paymentId;

    @OneToOne
    private Cart cart;

    @CreationTimestamp
    private LocalDate paymentDate;

    private String paymentType;

    private String walletId;
}
