package com.ashish.MyGenuineProtein.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String streetAddress;
    private String city;
    private String state;
    private String pinCode;
    private String landmark;
    private boolean isDelete;
    private  boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate createdAt;

    public String getFullAddress(){
        return streetAddress+", "+city+", "+state+", "+pinCode+", "+landmark;
    }

}
