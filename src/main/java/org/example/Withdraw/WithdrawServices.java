package org.example.Withdraw;

import org.example.Encryptor.Encryptor;
import org.example.Enums.Status;
import org.example.Exceptions.ApiException;
import org.example.User.User;
import org.example.User.UserRepository;

public class WithdrawServices {
    static UserRepository userRepository = UserRepository.getInstance();
    public static double Charge(String userId,double amount) throws ApiException
    {
        User user =userRepository.findById(userId).orElseThrow(()->new ApiException(Status.NOT_FOUND,"User doesnot exist in system please contact support"));
        userRepository.updateCredits(userId,-amount);
        user =userRepository.findById(userId).orElseThrow(()->new ApiException(Status.NOT_FOUND,"User doesnot exist in system please contact support"));
        return  user.getCredits();

    }
}
