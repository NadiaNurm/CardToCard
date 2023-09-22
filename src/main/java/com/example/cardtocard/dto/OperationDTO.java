package com.example.cardtocard.dto;

import lombok.Data;

import java.util.Date;
@Data
public class OperationDTO {
    private Date date;
    private String code;
    private String currency;
    private int amount;
    private String baseCardNumber;
    private String targetCardNumber;

    public OperationDTO(Date date, String code, String currency, int amount, String baseCardNumber, String targetCardNumber) {
        this.date = date;
        this.code = code;
        this.currency = currency;
        this.amount = amount;
        this.baseCardNumber = baseCardNumber;
        this.targetCardNumber = targetCardNumber;
    }




}
