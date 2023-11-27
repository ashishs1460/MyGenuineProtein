package com.ashish.MyGenuineProtein.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceTable{

    private String productName;

    private Integer quantity;

    private Double unitPrice;

    private Float subTotal;

    private Float discount;



}
