package com.example.cardtocard.controllers;

import com.example.cardtocard.Util.CardUtil;
import com.example.cardtocard.Util.OperationUtil;
import com.example.cardtocard.dto.OperationDTO;
import com.example.cardtocard.models.*;
import com.example.cardtocard.parser.ExchangeApi;
import com.example.cardtocard.services.CardService;
import com.example.cardtocard.services.OperationService;
import com.example.cardtocard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class CardController {
    @Autowired
    UserService userService;
    @Autowired
    CardService cardService;
    @Autowired
    OperationService operationService;

    @GetMapping("/creditCard")
    public String creditCard(Model model, @AuthenticationPrincipal User user) throws IOException {
        String fullName = user.getName() + " " + user.getSurname();
        model.addAttribute("fullName", fullName);
        model.addAttribute("cardNumber", CardUtil.createCardNumber());
        model.addAttribute("date", CardUtil.setDate());
        model.addAttribute("userId", user.getId());
        model.addAttribute("currencies", Currency.values());
        model.addAttribute("exchangeList", ExchangeApi.exchangeList());
        return "creditCard";
    }

    @PostMapping("/creditCard")
    public String creditCard(@RequestParam(name = "cardNumber") String cardNumber,
                             @RequestParam(name = "date") String date, @RequestParam(name = "userId") String userId,
                             @RequestParam(name = "currencies") String currencies) {
        User user = userService.loadUserById(Long.valueOf(userId));

        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(0));
        card.setUser(user);
        card.setNumber(cardNumber);
        card.setDate(date);
        card.setCurrency(Currency.valueOf(currencies));
        cardService.addCard(card);
        return "redirect:/mainPage";
    }

    @GetMapping("/manageCard/{id}")
    public String manageCard(@PathVariable(name = "id") String id, Model model, @AuthenticationPrincipal User user) throws IOException {
        String fullName = user.getName() + " " + user.getSurname();
        Card card = cardService.getCardById(Long.valueOf(id));
        model.addAttribute("fullName", fullName);
        model.addAttribute("cardNumber", card.getNumber());
        model.addAttribute("date", card.getDate());
        model.addAttribute("userId", user.getId());
        model.addAttribute("currency", card.getCurrency());
        model.addAttribute("currencies", Currency.values());
        model.addAttribute("balance", card.getBalance());
        model.addAttribute("cardId", card.getId());
        model.addAttribute("exchangeList", ExchangeApi.exchangeList());
        return "manageCard";
    }

    @PostMapping("/manageCard")
    public String manageCard(@RequestParam(name = "currencies") String currencies, @RequestParam(name = "newBalance") String newBalance,
                             @RequestParam(name = "cardId") String cardId, Model model) throws IOException {
        Card card = cardService.getCardById(Long.valueOf(cardId));
        BigDecimal bdec = new BigDecimal(newBalance);
        BigDecimal result = ExchangeApi.pairExchange(card.getCurrency().toString(), currencies, bdec);
        card.setBalance(card.getBalance().add(result));
        cardService.addCard(card);
        String fullName = card.getUser().getName() + " " + card.getUser().getSurname();
        Operation operation = new Operation();
        operation.setAmount(newBalance);
        operation.setCurrency(currencies);
        operation.setBaseCard(card);
        operation.setTargetCard(card);
        operation.setDate(new Date());
        operation.setCode(CardUtil.createOperationNumber());
        operationService.saveOperation(operation);

        model.addAttribute("fullName", fullName);
        model.addAttribute("cardNumber", card.getNumber());
        model.addAttribute("date", card.getDate());
        model.addAttribute("userId", card.getUser().getId());
        model.addAttribute("currency", card.getCurrency());
        model.addAttribute("currencies", Currency.values());
        model.addAttribute("balance", card.getBalance());
        model.addAttribute("cardId", card.getId());
        return "/manageCard";
    }


    @GetMapping("/confirmTransfer")
    public String confirmTransfer(@RequestParam(name = "cardId") String cardId, @RequestParam(name = "transferNumber") String transferNumber, Model model) {
        Card baseCard = cardService.getCardById(Long.valueOf(cardId));
        try {
            Card targetCard = cardService.getCardByNumber(transferNumber);
            String fullNameTarget = targetCard.getUser().getName() + " " + targetCard.getUser().getSurname();
            model.addAttribute("fullNameTarget", fullNameTarget);
            model.addAttribute("transferNumber", targetCard.getNumber());
            model.addAttribute("baseCard", baseCard.getNumber());
            model.addAttribute("currency", targetCard.getCurrency());
            model.addAttribute("cardId", cardId);
            model.addAttribute("currencies", Currency.values());
            model.addAttribute("defaultCurr",targetCard.getCurrency());

        } catch (java.lang.NullPointerException e) {
            model.addAttribute("fullNameTarget", "Wrong number");
        }

        return "/confirmTransfer";
    }

    @PostMapping("/confirmTransfer")
    public String confirmTransfer(@RequestParam(name = "cardId") String cardId,
                                  @RequestParam(name = "transferNumber") String transferNumber, Model model,
                                  @RequestParam(name = "transferAmount") String transferAmount, @RequestParam(name = "currencies") String currencies) throws IOException {

        Card baseCard = cardService.getCardById(Long.valueOf(cardId));
        Card targetCard = cardService.getCardByNumber(transferNumber);
        boolean f = cardService.doubleExchange(baseCard.getCurrency().toString(), targetCard.getCurrency().toString(), currencies,transferAmount,cardId,transferNumber);
        if(!f){
            model.addAttribute("error","Not enough money");

            String fullNameTarget = targetCard.getUser().getName() + " " + targetCard.getUser().getSurname();
            model.addAttribute("fullNameTarget", fullNameTarget);
            model.addAttribute("transferNumber", targetCard.getNumber());
            model.addAttribute("baseCard", baseCard.getNumber());
            model.addAttribute("currency", targetCard.getCurrency());
            model.addAttribute("cardId", cardId);
            model.addAttribute("currencies", Currency.values());
            model.addAttribute("defaultCurr",targetCard.getCurrency());
            return "/confirmTransfer";
        }
        return "redirect:/mainPage";
    }

    //@GetMapping("/operations/{id}")
    //public String filterProducts(@PathVariable (name = "id") String id, Model model, PriceRange priceRange){
    //    List<Operation> operations = operationService.allCardOperations(Long.valueOf(id),Long.valueOf(id));
    //    List<OperationDTO> operationDTOList = OperationUtil.crteateOperationDTO(operations);
    //    model.addAttribute("operations",operationService.filterOperations(operationDTOList, priceRange.getMin(), priceRange.getMax()));
    //    //model.addAttribute("operations", operationDTOList);
    //    return "operations";
    //}

    @GetMapping("/operations")
    public String filterProducts(Model model, PriceRange priceRange){
        List<Operation> operations = operationService.allOperations();
        List<OperationDTO> operationDTOList = OperationUtil.crteateOperationDTO(operations);
        List<OperationDTO> filter = operationService.filterOperations(priceRange.getMin(), priceRange.getMax());
        model.addAttribute("operations",filter);
        //model.addAttribute("operations", operationDTOList);
        return "operations";
    }



}
