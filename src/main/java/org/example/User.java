package org.example;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String Id;
    private double Credits;
    private String email;
    private String password;
    private Visa visa;
    private String Token;

    User(JSONObject object){
        firstName=object.getJSONObject("firstName").toString();
        lastName=object.getString("lastName");
        Id="US"+UUID.randomUUID().toString();
        Credits=0;
        email=object.getString("");

    }


}
