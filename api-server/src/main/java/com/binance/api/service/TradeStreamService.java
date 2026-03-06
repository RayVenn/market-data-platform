package com.binance.api.service;

import com.binance.api.model.TradeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TradeStreamService {

    private static final Logger log = LoggerFactory.getLogger(TradeStreamService.class);

    private final SimpMessagingTemplate messaging;
    private final ObjectMapper          mapper = new ObjectMapper();

    public TradeStreamService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    // crypto-trades uses Binance wire-format short keys: "s", "p", "q", "T", "m", "t"
    @KafkaListener(topics = "${app.kafka.trades-topic:crypto-trades}",
                   groupId = "api-server-trades")
    public void onTrade(String raw) {
        try {
            JsonNode node = mapper.readTree(raw);

            TradeDto dto  = new TradeDto();
            dto.symbol    = node.path("s").asText();
            dto.source    = node.path("source").asText();
            dto.tradeId   = node.path("t").asLong();
            dto.price     = node.path("p").asDouble();
            dto.quantity  = node.path("q").asDouble();
            dto.side      = node.path("m").asBoolean() ? "SELL" : "BUY";
            dto.tradeTime = Instant.ofEpochMilli(node.path("T").asLong()).toString();
            dto.latencyMs = node.path("latencyMs").asLong();

            messaging.convertAndSend("/topic/trades/" + dto.symbol, dto);
        } catch (Exception e) {
            log.warn("trade_stream.parse_error error={}", e.getMessage());
        }
    }
}
