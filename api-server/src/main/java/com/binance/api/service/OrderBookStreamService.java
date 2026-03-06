package com.binance.api.service;

import com.binance.api.model.OrderBookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderBookStreamService {

    private static final Logger log = LoggerFactory.getLogger(OrderBookStreamService.class);

    private final SimpMessagingTemplate messaging;
    private final ObjectMapper          mapper = new ObjectMapper();

    public OrderBookStreamService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @KafkaListener(topics = "${app.kafka.orderbook-topic:crypto-orderbook}",
                   groupId = "api-server-orderbook")
    public void onOrderBook(String raw) {
        try {
            // Producer sends { source, symbol, timestampMs (long), bids, asks }
            // Map timestampMs → snapshotTime ISO string before populating the DTO
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(raw);

            OrderBookDto dto  = new OrderBookDto();
            dto.source        = node.path("source").asText();
            dto.symbol        = node.path("symbol").asText();
            dto.snapshotTime  = java.time.Instant.ofEpochMilli(node.path("timestampMs").asLong()).toString();
            dto.bids          = mapper.convertValue(node.path("bids"),
                    mapper.getTypeFactory().constructCollectionType(
                            java.util.List.class, com.binance.api.model.PriceLevelDto.class));
            dto.asks          = mapper.convertValue(node.path("asks"),
                    mapper.getTypeFactory().constructCollectionType(
                            java.util.List.class, com.binance.api.model.PriceLevelDto.class));

            messaging.convertAndSend("/topic/orderbook/" + dto.symbol, dto);
        } catch (Exception e) {
            log.warn("orderbook_stream.parse_error error={}", e.getMessage());
        }
    }
}
