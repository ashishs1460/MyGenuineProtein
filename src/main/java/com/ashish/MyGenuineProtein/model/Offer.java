package com.ashish.MyGenuineProtein.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private  long id;

    private int categoryOffPercentage;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "category_id")
    private  Category category;

    @OneToOne
    @JoinColumn(name = "product_id")
    private  Product product;

    private  int count;

}
