package com.ashish.MyGenuineProtein.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private Cart cart;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Long Quantity;
}
