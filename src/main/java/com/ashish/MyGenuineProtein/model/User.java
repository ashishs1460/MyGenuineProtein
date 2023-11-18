package com.ashish.MyGenuineProtein.model;


import com.ashish.MyGenuineProtein.otp.model.Otp;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column(name = "user_id")
    private UUID id;

    @Getter
    @NotEmpty(message = "is required")
//    @Column(nullable = false)
    private String firstName;

    @NotEmpty(message = "is required")
    private String lastName;

//    @Column(nullable = false,unique = true)
    @NotEmpty(message = "is required")
    @Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "is not valid")
//    @Email(message = "{errors.invalid_email}")
    private String email;

    @NotEmpty
    private String password;

    private boolean isActive;

    private String userReferralCode;

    private boolean verified;
    @OneToOne(cascade = CascadeType.ALL)
    private Otp otp;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")},
    inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",referencedColumnName = "ROLE_ID")})
    private List<Role> roles;

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name ="cart_id")
    private Cart cart;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wishList_id")
    private WishList wishList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet ;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private  List<Review> reviews = new ArrayList<>();


    public User(User user) {

        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.userReferralCode=user.getUserReferralCode();
    }

    public User(){

    }

}
