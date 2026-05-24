package org.example.SignUp;

import lombok.Getter;
import lombok.Setter;
import org.example.Exceptions.ApiException;
import org.example.Encryptor.Encryptor;
import org.example.Enums.CardType;
import org.example.Enums.Roles;
import org.example.Enums.Status;
import org.example.User.User;
import org.example.User.Visa;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Setter
@Getter

public class SignUpService {


    public static User SignUp (JSONObject object) throws ApiException {
    User user =new User();
        //*Parse name
        user.setName(parseName(object)) ;

        user.setFirstName(user.getName().substring(0, user.getName().lastIndexOf(" ")));
        user.setLastName ( user.getName().substring(user.getName().lastIndexOf(" ") + 1));





        //*Parse Email
        user.setEmail(parseEmail(object));



        //*Parse address
       user.setAddress(parseAddress(object)) ;


        //*parse Password
        user.setPassword( parsePassword(object));

        //*Parse phoneNumber

            user.setPhoneNumber(phoneNumberParse(object)) ;



        //*Parse Role
        user.setRole(parseRole(object)) ;



        //*parse visa
        user.setVisa( buildVisa(object)) ;




        //*Genrate Random id
        user.setId( "US" + UUID.randomUUID());
        user.setCredits(0);
        return user;

    }


    private static Roles parseRole(JSONObject object) throws ApiException {
        try {
            return Roles.valueOf(object.getString("role"));
        } catch (Exception ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Invalid role"
            );
        }
    }
    private static String parseAddress(JSONObject object) throws ApiException {
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
    private static String parseEmail(JSONObject object) throws ApiException {
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
    private static String parseName(JSONObject object) throws ApiException {
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

    private static Visa buildVisa(JSONObject object) throws ApiException {

        Visa visa = new Visa();

        try {
            visa.setCardNo(object.getLong("cardNumber"));
        } catch (JSONException e) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing CardNo"
            );
        }
        if (visa.getCardNo() < (1e16) && visa.getCardNo() > (1e19)) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Invalid Card No"
            );
        }
        try {
            visa.setCvv(Integer.parseInt(
                    object.getString("cvv")));
        } catch (
                NumberFormatException ex) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "CVV must contain numbers only");
        }
        if (visa.getCvv() < 100 || visa.getCvv() > 999) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "CVV must be 3 digits");
        }
        try {
            visa.setCardType(CardType.valueOf(
                    object.getString("cardType")));
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


            visa.setCardExpiry(YearMonth.parse(object.getString("mm/yy"), formatter));

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
        try {
            visa.setCardCarrier(object.getString("cardCarrier"));

        } catch (JSONException e) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing cardCarrier"
            );
        }try {
            String password=object.getString("cardConfirm");
           if(!password.matches("^\\d{4}(\\d{2})?$"))
            {
                       throw new ApiException(
                    Status.BAD_REQUEST,
                    "Bad cardConfirm formate"
            );
            }
           Encryptor hashed =new Encryptor(password);
                    visa.setPassword(hashed.getHashed() );

        } catch (JSONException e) {
            throw new ApiException(
                    Status.BAD_REQUEST,
                    "Missing cardConfirm"
            );
        }


        return visa;

    }

        private static String parsePassword(JSONObject object) throws ApiException {
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
        }private static String phoneNumberParse(JSONObject object) throws ApiException {
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
