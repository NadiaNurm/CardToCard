package com.example.cardtocard.controllers;

import com.example.cardtocard.Util.OperationUtil;
import com.example.cardtocard.dto.OperationDTO;
import com.example.cardtocard.models.Operation;
import com.example.cardtocard.models.PriceRange;
import com.example.cardtocard.services.OperationService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class IndexController {
    @Autowired
    OperationService operationService;

    @GetMapping("/history/{opId}")
    public String main(Model model, @PathVariable(name = "opId") String id) {
        Set<String> allTargetCards = operationService.allTargetCards();
        model.addAttribute("allTargetCards",allTargetCards);
        List<Operation> operations = operationService.allCardOperations(Long.valueOf(id), Long.valueOf(id));
        model.addAttribute("priceRange", new PriceRange(1, 1000));
        model.addAttribute("operations", operations);
        return "history";
    }

    @PostMapping("/history")
    public String save(PriceRange priceRange, Model model) {
        List<OperationDTO> filtered = operationService.filterOperations(priceRange.getMin(), priceRange.getMax());
        model.addAttribute("maxRange", filtered.stream().map(OperationDTO::getAmount).max(Long::compare).get());
        model.addAttribute("minRange", filtered.stream().map(OperationDTO::getAmount).min(Long::compare).get());
        return "saved";
    }

    @PostMapping("/targetCards")
    public String targetCards(@RequestParam(name = "allTargetCards") String allTargetCards, Model model){
        model.addAttribute("operations",operationService.targetCards(allTargetCards));
        return "operations";
    }


}
