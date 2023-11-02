package com.ashish.MyGenuineProtein.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private  Variant variant;

    private  Integer quantity;
    private double variantPrice;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
