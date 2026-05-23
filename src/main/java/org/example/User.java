package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Enums.CardType;
import org.example.Enums.Roles;
import org.example.Enums.Status;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String Name;
    private String firstName;
    private String lastName;
    private String Id;
    private double Credits;
    private String email;
    private String password;
    private Visa visa;
    private Roles role;
    private String address;
    private String phoneNumber;


    }
