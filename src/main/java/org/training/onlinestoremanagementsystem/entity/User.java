package org.training.onlinestoremanagementsystem.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    private String firstName;

    private String lastName;

    private String emailId;

    private int age;

    private String password;

    private String contactNumber;

    private Role role;

    public User(){
        this.userId = UUID.randomUUID();
    }
}
