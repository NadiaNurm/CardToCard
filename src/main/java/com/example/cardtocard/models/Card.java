package com.example.cardtocard.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal balance;
    private String number;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    @ManyToOne
    private User user;
    private String date;
    @Transient
    private String showNumber;

    public String getShowNumber(){
        return this.number.substring(0,15).replaceAll("\\d","*") + this.number.substring(15);
    }
    public void setShowNumber(){
        this.showNumber = this.number.substring(0,15).replaceAll("\\d","*") + this.number.substring(15);
    }

    public Card(Long id, BigDecimal balance, String number, Currency currency, User user, String date, String showNumber) {
        this.id = id;
        this.balance = balance;
        this.number = number;
        this.currency = currency;
        this.user = user;
        this.date = date;
        this.showNumber = showNumber;
    }
    public Card() { }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
