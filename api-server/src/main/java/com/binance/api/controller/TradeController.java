package com.binance.api.controller;

import com.binance.api.model.TradeDto;
import com.binance.api.repository.TradeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TradeController {

    private final TradeRepository tradeRepo;

    public TradeController(TradeRepository tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    @GetMapping("/trades")
    public List<TradeDto> getTrades(
            @RequestParam String symbol,
            @RequestParam(defaultValue = "100") int limit) {
        return tradeRepo.findRecent(symbol, Math.min(limit, 1000));
    }
}
