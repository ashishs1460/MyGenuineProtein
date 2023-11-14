package com.ashish.MyGenuineProtein.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Category {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "category_id")
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @OneToOne(mappedBy = "category")
    private Offer offer;






}

