package com.example.cardtocard.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    @ManyToOne
    private Card baseCard;
    @ManyToOne
    private Card targetCard;
    private String code;
    private String currency;
    private String amount;


}
