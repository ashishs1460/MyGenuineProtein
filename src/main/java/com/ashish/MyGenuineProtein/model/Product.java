package com.ashish.MyGenuineProtein.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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


    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
     private List<Variant> variants;

    @OneToOne(mappedBy = "product")
    private Offer offer ;
//    public List<Offer> getOffers() {
//        return offer;
//    }
//    private  float discountedPrice = 0.0f;

    private String description;

    private boolean isDelete;

    @OneToMany( mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductImage> productImages = new ArrayList<>();

}
