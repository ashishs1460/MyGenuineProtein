package com.ashish.MyGenuineProtein.otp.model;


import com.ashish.MyGenuineProtein.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String otp;

    LocalDateTime time;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    User user;

}
