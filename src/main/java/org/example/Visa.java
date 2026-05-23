package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.CardType;

import java.time.YearMonth;

@Setter
@Getter
public class Visa {

    long CardNo;
    int   Cvv;
    CardType CardType;
    YearMonth CardExpiry;
    int Password;
    String CardCarrier;
}
