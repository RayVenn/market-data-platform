package com.binance.api.controller;

import com.binance.api.model.Trade;
import com.binance.api.model.TradeDto;
import com.binance.api.repository.TradeRepository;
import org.springframework.data.domain.PageRequest;
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
        return tradeRepo.findRecentBySymbol(symbol, PageRequest.of(0, Math.min(limit, 1000)))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private TradeDto toDto(Trade t) {
        TradeDto dto   = new TradeDto();
        dto.tradeTime  = t.id.tradeTime.toString();
        dto.symbol     = t.id.symbol;
        dto.source     = t.id.source;
        dto.tradeId    = t.id.tradeId;
        dto.price      = t.price;
        dto.quantity   = t.quantity;
        dto.side       = t.side;
        dto.latencyMs  = t.latencyMs;
        return dto;
    }
}
