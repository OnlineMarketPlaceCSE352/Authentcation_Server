package org.example.Payment;

import org.example.Exceptions.ApiException;
import org.example.Enums.Status;
import org.example.Parser.Response;
import org.example.User.UserRepository;
import org.json.JSONObject;

public class PaymentServices {
   static UserRepository userRepository = UserRepository.getInstance();
    public static JSONObject Payment(String sellerId, String costumerId, double amount) throws ApiException {
        /// fetching the Costumer Id

        Double costumerCredit = userRepository.getCreditsById(costumerId).orElseThrow(()->new ApiException(Status.BAD_REQUEST,"Missing credit"));
        if (costumerCredit < amount) {
            throw new ApiException(Status.BAD_REQUEST, "No Sufficient Funds");
        }
        /// adding funds to the Seller
        userRepository.updateCredits(sellerId, amount);
        userRepository.updateCredits(costumerId, -amount);

        /// return to the handler the credit now
         costumerCredit = userRepository.getCreditsById(costumerId).orElseThrow(()->new ApiException(Status.BAD_REQUEST,"Missing credit"));
       String sellerEmail = userRepository.findById(sellerId).orElseThrow().getEmail();
       String costumerEmail = userRepository.findById(costumerId).orElseThrow().getEmail();
       JSONObject json = new JSONObject();
       json.put("sellerEmail",sellerEmail);
       json.put("buyerEmail",costumerEmail);
       json.put("credits",costumerCredit);
        return  json;


    }
}
