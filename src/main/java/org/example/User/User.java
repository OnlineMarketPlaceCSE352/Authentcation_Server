package org.example.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.Enums.Roles;


@Setter
@Getter
@AllArgsConstructor

@Entity
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(90)")
    private String id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "firstName",nullable = false)
    private String firstName;
    @Column(name = "lastName",nullable = false)
    private String lastName;

    @Column(name = "credits", nullable = false, columnDefinition = "DECIMAL(18,2)")
    private double credits;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_no")
    private Visa visa;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;


    }
