package org.example.Enums;

import org.example.ApiException;

public class UserRepository {
    public static double getCreditById(String sellerId) throws ApiException
    {
        /// get user credit
        ///  if user doesnot exist throw API Exception
        return 9999999;
    }

    public synchronized static void addCreditById(String sellerId,double amount) throws  ApiException
    {
        /// change  user credit
        ///  if user doesnot exist throw API Exception


    }
    public synchronized static void chargeCreditById(String sellerId,double amount) throws  ApiException
    {
        /// change  user credit
        ///  if user doesnot exist throw API Exception


    }
}
