package com.ashish.MyGenuineProtein.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Product {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column(name = "product_id")
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",referencedColumnName = "category_id")
    private Category category;

    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flavour_id",referencedColumnName = "flavour_id")
    private Flavour flavour;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weight_id",referencedColumnName = "weight_id")
    private Weight weight;

    private String description;

    private String imageName;

}
