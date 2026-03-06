package com.binance.api.controller;

import com.binance.api.model.OrderBookDto;
import com.binance.api.repository.OrderBookRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderBookController {

    private final OrderBookRepository orderBookRepo;

    public OrderBookController(OrderBookRepository orderBookRepo) {
        this.orderBookRepo = orderBookRepo;
    }

    @GetMapping("/orderbook")
    public List<OrderBookDto> getOrderBook(@RequestParam String symbol) {
        return orderBookRepo.findLatestPerExchange(symbol);
    }
}
