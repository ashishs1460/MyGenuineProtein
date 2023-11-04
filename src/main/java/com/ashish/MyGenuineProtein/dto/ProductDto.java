package com.ashish.MyGenuineProtein.dto;


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


    private String description;

}
