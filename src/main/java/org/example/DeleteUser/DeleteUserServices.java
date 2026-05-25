package org.example.DeleteUser;

import org.example.Exceptions.ApiException;
import org.example.User.UserRepository;

public class DeleteUserServices {
    static UserRepository userRepository = UserRepository.getInstance();
public static void delete(String Id) throws ApiException
{

    userRepository.delete(Id);
}
}
