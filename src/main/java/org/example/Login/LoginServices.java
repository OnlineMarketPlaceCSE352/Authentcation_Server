package org.example.Login;

import org.example.Exceptions.ApiException;
import org.example.Encryptor.Encryptor;
import org.example.Enums.Status;
import org.example.User.User;
import org.example.User.UserRepository;

public class LoginServices {
static UserRepository userRepository = UserRepository.getInstance();
    public static User Login(String email, String password ) throws ApiException
{
    //!fetching the Hashed pass
    User user = userRepository.findByEmail(email).orElseThrow(()->new ApiException(Status.NOT_FOUND,"Email not found"));
//     System.out.println(user.getPassword());
    if(!Encryptor.Check2Pass(password,user.getPassword()))
        {
            throw new ApiException(Status.UNAUTHORIZED,"Password is not correct");
        }
        return user;

}
}
