package org.example.SearchUser;

import org.example.Enums.Roles;
import org.example.Parser.Response;
import org.example.User.User;
import org.example.User.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SearchUserServices {
    static UserRepository userRepository = UserRepository.getInstance();
    public static JSONArray Search(Roles role, String name) {
        List<User> users = userRepository.searchByName(name);
        JSONArray usersArray = null;
        if (role.equals(Roles.Admin)) {
            usersArray = new JSONArray();
            for (User u : users) {
                JSONObject userObject =
                        new JSONObject();
                userObject.put("name", u.getName());
                userObject.put("email", u.getEmail());
                userObject.put("id", u.getId());
                userObject.put("address", u.getAddress());
                userObject.put("phoneNumber", u.getPhoneNumber());
                userObject.put("newBalance", u.getCredits());
                userObject.put("role", u.getRole());
                userObject.put("cardNumber", u.getVisa().getCardNo());
                userObject.put("cvv", u.getVisa().getCvv());
                userObject.put("cardType", u.getVisa().getCardType());
                userObject.put("mm/yy", u.getVisa().getCardExpiry());
                userObject.put("cardCarrier", u.getVisa().getCardCarrier());
                usersArray.put(userObject);

            }
        }
        else{
            usersArray = new JSONArray();
            for (User u : users) {
                JSONObject userObject = new JSONObject();
                userObject.put("name", u.getName());
                userObject.put("email", u.getEmail());
                userObject.put("phoneNumber", u.getPhoneNumber());
                usersArray.put(userObject);

            }
        }
        System.out.println("==================Printing users========");
        System.out.println(usersArray.toString());
        return usersArray;
    }
}
