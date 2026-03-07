package com.binance.api.controller;

import com.binance.api.model.OrderBookDto;
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

    // Orderbook is served via WebSocket (/topic/orderbook/{symbol}).
    // No persistent snapshot table — return empty list on initial load.
    @GetMapping("/orderbook")
    public List<OrderBookDto> getOrderBook(@RequestParam String symbol) {
        return List.of();
    }
}
