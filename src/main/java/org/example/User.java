package org.example;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String Id;
    private int Age;
    private double Credits;
    private String email;
    private String password;
    private Visa visa;
    private String Token;



}
