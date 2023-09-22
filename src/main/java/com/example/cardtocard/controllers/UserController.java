package com.example.cardtocard.controllers;

import com.example.cardtocard.models.Card;
import com.example.cardtocard.models.User;
import com.example.cardtocard.parser.ExchangeApi;
import com.example.cardtocard.services.CardService;
import com.example.cardtocard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

//http://localhost:8080/
@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CardService cardService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userObject", new User());
        model.addAttribute("error", "");
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userObject") User user, Model model) {
        if (userService.confirmPassword(user)) {
            if (!userService.existsByUsername(user.getUsername())) {
                userService.addUser(user);
            } else {
                model.addAttribute("error", "Юзер существует!");
            }
        } else {
            model.addAttribute("error", "Пароли не совпадают!");
        }
        return "registration";
    }

    @GetMapping("/mainPage")
    public String mainPage(@AuthenticationPrincipal User user, Model model) throws IOException {
        List<Card> cardList = cardService.getCardsByUser(user);
        model.addAttribute("cardList", cardList);
        model.addAttribute("exchangeList", ExchangeApi.exchangeList());
        return "mainPage";
    }
}