package org.example.Profile;

import org.example.Exceptions.ApiException;
import org.example.Enums.Status;
import org.example.User.User;
import org.example.User.UserRepository;

public class ProfileServices {
    static UserRepository userRepository = UserRepository.getInstance();
    public static User GetProfile(String userId) throws ApiException {

       User user = userRepository.findById(userId).orElseThrow(()->new ApiException(Status.NOT_FOUND,"User Profile Missing Please Contact Support"));
        return user;

    }
}
