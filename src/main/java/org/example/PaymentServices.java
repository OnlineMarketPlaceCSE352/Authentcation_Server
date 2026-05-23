package org.example;

import org.example.Enums.Status;

public class PaymentServices {
   static UserRepository userRepository = UserRepository.getInstance();
    public static double Payment(String sellerId, String costumerId, double amount) throws ApiException {
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
        return costumerCredit;


    }
}
