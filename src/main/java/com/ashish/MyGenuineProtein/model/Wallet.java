package com.ashish.MyGenuineProtein.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
   private User user;

    private double amount ;

    private double referralAmount;


}
