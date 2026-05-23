package org.example;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

@Entity
public class User {
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @Column(name = "firstName",nullable = false)
    private String firstName;
    @Column(name = "lastName",nullable = false)
    private String lastName;

    @Column(name = "credits", nullable = false, columnDefinition = "DECIMAL(18,2)")
    private double Credits;

    @Column(name = "email",nullable = false)
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

    User(JSONObject object) throws ApiException {
        //*Parse name
        Name = parseName(object);

        firstName = Name.substring(0, Name.lastIndexOf(" "));
        lastName = Name.substring(Name.lastIndexOf(" ") + 1);





        //*Parse Email
        email=parseEmail(object);



        //*Parse address
       address =parseAddress(object);


        //*parse Password
        password = parsePassword(object);

        //*Parse phoneNumber

        phoneNumber =phoneNumberParse(object);



        //*Parse Role
        role = parseRole(object);



        //*parse visa
        visa = buildVisa(object);




        //*Genrate Random id
        Id = "US" + UUID.randomUUID();
        Credits=0;

    }

    private Roles parseRole(JSONObject object) throws ApiException {
        try {
            return Roles.valueOf(object.getString("role"));
        } catch (Exception ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Invalid role"
            );
        }
    }
    private String parseAddress(JSONObject object) throws ApiException {
        String add;
        try {
            add = object.optString("address");
        } catch (JSONException ex) {
            //?Missing address key
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing  address ");
        }
        //?addres validation
        if (!add.matches("^[A-Za-z0-9\\s,.\\-/#]{5,100}$")) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "User address invalid ");
        }
        return add;
    }
    private String parseEmail(JSONObject object) throws ApiException {
       String e;
        try {
            e = object.optString("email");
        } catch (JSONException ex) {
            //?Missing email key
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing email address ");
        }
        //?email validation
        if (!e.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Email address invalid ");
        }
        return e;
    }
    private String parseName(JSONObject object) throws ApiException {
        String N;
        try {
            N = object.getString("name");
        } catch (JSONException ex) {
            //?Missing key for name
            throw new ApiException(Status.BAD_REQUEST,
                    "Missing Name");
        }
        if (!N.contains(" ")) {
            //?The Name is not full Name
            throw new ApiException(Status.BAD_REQUEST,
                    "Invalid full name");
        }
        return  N;
    }

    private Visa buildVisa(JSONObject object) throws ApiException {

        Visa visa = new Visa();

        try {
            visa.CardNo = object.getLong("cardNumber");
        } catch (JSONException e) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing CardNo"
            );
        }
        if (visa.CardNo < (1e16) && visa.CardNo > (1e19)) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Invalid Card No"
            );
        }
        try {
            visa.Cvv = Integer.parseInt(
                    object.getString("cvv"));
        } catch (
                NumberFormatException ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "CVV must contain numbers only");
        }
        if (visa.Cvv < 100 || visa.Cvv > 999) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "CVV must be 3 digits");
        }
        try {
            visa.CardType =
                        CardType.valueOf(
                            object.getString("cardType"));
        } catch (
                JSONException ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing Card Type");
        } catch (IllegalArgumentException ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Invalid Card Type");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");


            visa.CardExpiry = YearMonth.parse(object.getString("mm/yy"), formatter);
            visa.CardCarrier = object.getString("cardCarrier");

        } catch (
                DateTimeParseException ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Invalid Visa Date Formate");
        }
        catch (
                JSONException ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing Date");
        }
        return visa;

    }

    private String parsePassword (JSONObject object) throws ApiException {
            String p;
            Encryptor H;
            //*Parsing the password
            try {
                p = object.getString("password");
            } catch (JSONException ex) {
                //?missing password
                throw new ApiException(Status.BAD_REQUEST, "Missing Password");
            }
            if (!p.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
                throw new ApiException(
                        //?Password Validation
                        Status.BAD_REQUEST,
                        "Password must contain uppercase, lowercase, number, special character and be at least 8 characters"
                );
            }
            H = new Encryptor(p);
            return (H.Hashed);
        }
    private String phoneNumberParse(JSONObject object) throws ApiException {
        String pn;
        try {
             pn = object.getString("phoneNumber");
        } catch (JSONException ex) {
            //?Missing phoneNumber key
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing  PhoneNumber ");
        }
        //?phoneNumber validation
        if (!(pn.matches("^[A-Za-z0-9\\s,.-]{5,100}$"))) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing user Phone Number ");
        }
        return  pn;
        }
    }
