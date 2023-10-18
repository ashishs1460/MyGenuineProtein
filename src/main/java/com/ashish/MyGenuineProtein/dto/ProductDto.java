package com.ashish.MyGenuineProtein.dto;


import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.Flavour;
import com.ashish.MyGenuineProtein.model.Weight;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@ToString
public class ProductDto {

    private UUID id;

    private String name;


    private UUID categoryId;

    private double price;


    private UUID flavourId;



    private UUID weightId;

    private String description;

    private String imageName;
}
