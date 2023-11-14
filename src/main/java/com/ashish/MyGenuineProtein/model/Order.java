package com.ashish.MyGenuineProtein.model;


import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<OrderItems> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address address;

    @Enumerated(EnumType.STRING)
    PaymentMode paymentMode;

    @Column(length = 20) // Adjust the length according to your needs
    @Enumerated(EnumType.STRING)
    Status status;
    Double totalPrice;

    private LocalDate orderDate;
    private LocalDate shippingDate;
    private LocalDate packingDate;
    private LocalDate deliveryDate;
}
