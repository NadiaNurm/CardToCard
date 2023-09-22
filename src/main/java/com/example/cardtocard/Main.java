package com.example.cardtocard;

import com.example.cardtocard.Util.CardUtil;

public class Main {
    public static void main(String[] args) {
        String number = CardUtil.createCardNumber();

        String newNumber = number.substring(0,15).replaceAll("\\d","*") + number.substring(15);
        System.out.println(newNumber);
    }
}
