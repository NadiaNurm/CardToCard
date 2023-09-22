package com.example.cardtocard.repo;

import com.example.cardtocard.models.Card;
import com.example.cardtocard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
        public List<Card> findByUser(User user);


        public Card findByNumber(String number);
}
