package com.example.cardtocard.services;

import com.example.cardtocard.Util.CardUtil;
import com.example.cardtocard.models.Card;
import com.example.cardtocard.models.Operation;
import com.example.cardtocard.models.User;
import com.example.cardtocard.parser.ExchangeApi;
import com.example.cardtocard.repo.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    OperationService operationService;

    public void addCard(Card card) {
        cardRepository.save(card);
    }

    public List<Card> getCardsByUser(User user) {
        return cardRepository.findByUser(user);
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id).get();
    }

    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    public boolean doubleExchange(String baseCurr, String targetCurr, String transferCurr, String transferAmount, String cardId, String transferNumber) throws IOException {
        Card baseCard = cardRepository.findById(Long.valueOf(cardId)).get();
        Card targetCard = cardRepository.findByNumber(transferNumber);
        BigDecimal amount = new BigDecimal(transferAmount);
        BigDecimal result = new BigDecimal(0);
        Operation operation = new Operation();
        operation.setAmount(transferAmount);
        operation.setCurrency(transferCurr);
        operation.setBaseCard(baseCard);
        operation.setTargetCard(targetCard);
        operation.setDate(new Date());
        operation.setCode(CardUtil.createOperationNumber());
        operationService.saveOperation(operation);
        if (baseCurr.equals(transferCurr)) {
            result = ExchangeApi.pairExchange(baseCurr, targetCurr, amount);
            if (CardUtil.checkBalance(baseCard, amount)) {
                baseCard.setBalance(baseCard.getBalance().subtract(amount));
                cardRepository.save(baseCard);
                targetCard.setBalance(targetCard.getBalance().add(result));
                cardRepository.save(targetCard);
                return true;
            }

        } else {
            BigDecimal b = ExchangeApi.pairExchange(baseCurr, transferCurr, amount);
            if (CardUtil.checkBalance(baseCard, b)) {
                baseCard.setBalance(baseCard.getBalance().subtract(b));

                BigDecimal b2 = ExchangeApi.pairExchange(targetCurr, baseCurr, b);
                targetCard.setBalance(targetCard.getBalance().add(b2));
                cardRepository.save(baseCard);
                cardRepository.save(targetCard);
                return true;
            }
        }
    return false;
    }




}
