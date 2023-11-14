package com.ashish.MyGenuineProtein.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<CartItems> cartItems;

    private double total;

    private String couponCode;

    private  double discount;






}
