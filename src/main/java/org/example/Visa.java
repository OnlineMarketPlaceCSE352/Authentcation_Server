package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
public class Visa {

    int CardNo;
    int   Cvv;
    String   CardType;
    Date CardExpiry;
    int Password;
    String CardCarrier;
}
