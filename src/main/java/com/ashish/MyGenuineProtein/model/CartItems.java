package com.ashish.MyGenuineProtein.model;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private Cart cart;


    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private int Quantity;

}
