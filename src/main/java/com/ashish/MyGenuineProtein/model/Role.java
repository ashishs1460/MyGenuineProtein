package com.ashish.MyGenuineProtein.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column(name = "role_id")
    private UUID id;

    @Column(nullable = false,unique = true)
    @NotEmpty
    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
