package org.example;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.Enums.CardType;

import java.time.YearMonth;

@Setter
@Getter

@Entity
public class Visa {
    @Id
    @Column(name = "card_no")
    long CardNo;

    @Column(name = "cvv")
    int Cvv;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    CardType CardType;

    @Column(name = "card_expiry")
    YearMonth CardExpiry;

    @Column(name = "password")
    int Password;

    @Column(name = "card_carrier")
    String CardCarrier;
}
