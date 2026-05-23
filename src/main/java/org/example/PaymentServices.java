package org.example;

import org.example.Enums.Status;
import org.example.Enums.UserRepository;

public class PaymentServices {
    public static double Payment(String sellerId, String costumerId, double amount) throws ApiException {
        /// fetching the Costumer Id

        double costumerCredit = UserRepository.getCreditById(costumerId);
        if (costumerCredit < amount) {
            throw new ApiException(Status.BAD_REQUEST, "No Sufficient Funds");
        }
        /// adding funds to the Seller
        UserRepository.addCreditById(sellerId, amount);
        UserRepository.chargeCreditById(costumerId, amount);

        /// return to the handler the credit now
         costumerCredit = UserRepository.getCreditById(costumerId);
        return costumerCredit;


    }
}
