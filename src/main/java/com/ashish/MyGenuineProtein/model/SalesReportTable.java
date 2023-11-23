package com.ashish.MyGenuineProtein.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SalesReportTable {

    private String orderId;

    private LocalDate orderDate;

    private String customer;

    private Double totalAmount;

    private String paymentMethod;

    private String products;

    private String orderStatus;
}
