package org.example.ChargeCredit;

import org.example.Exceptions.ApiException;
import org.example.Encryptor.Encryptor;
import org.example.Enums.Status;
import org.example.User.User;
import org.example.User.UserRepository;

public class ChargeCreditServices {
    static UserRepository userRepository = UserRepository.getInstance();
    public static double Charge(String userId,double amount,String password) throws ApiException
    {
        User user =userRepository.findById(userId).orElseThrow(()->new ApiException(Status.NOT_FOUND,"User doesnot exist in system please contact support"));
        if(!Encryptor.Check2Pass(password,user.getVisa().getPassword()))
        {
            throw new ApiException(Status.UNAUTHORIZED,"Visa OTP is incorrect please contact your Bank");
        }
        userRepository.updateCredits(userId,amount);
         user =userRepository.findById(userId).orElseThrow(()->new ApiException(Status.NOT_FOUND,"User doesnot exist in system please contact support"));
        return  user.getCredits();

    }
}
