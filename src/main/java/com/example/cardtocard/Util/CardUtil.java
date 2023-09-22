package com.example.cardtocard.Util;

import com.example.cardtocard.models.Card;
import com.example.cardtocard.models.Operation;
import com.example.cardtocard.repo.OperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CardUtil {
    public static void main(String[] args) {

    }

    public static String createCardNumber() {
        String number = "";
        for (int i = 0; i < 16; i++) {
            number += ((int) (Math.random() * 10)) + "";
        }
        char delimiter = ' ';
        return number.replaceAll(".{4}(?!$)", "$0" + delimiter);
    }

    public static String setDate() {
        LocalDate localDate = LocalDate.now();
        LocalDate end = localDate.plusYears(3);
        return end.getMonthValue() + "/" + end.getYear();
    }

    public static boolean checkBalance(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(amount) > 0) {
            return true;
        }
        return false;
    }

    public static String createOperationNumber(){
        String number = "";
        for (int i = 0; i < 10; i++) {
            number += ((int) (Math.random() * 10)) + "";
        }
        return number;
    }


}
