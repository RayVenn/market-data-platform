package com.binance.api.repository;

import com.binance.api.model.OrderBookDto;
import com.binance.api.model.PriceLevelDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderBookRepository {

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderBookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<OrderBookDto> findLatestPerExchange(String symbol) {
        String sql = """
                SELECT DISTINCT ON (source)
                    snapshot_time, symbol, source, bids, asks
                FROM orderbook_snapshots
                WHERE symbol = ?
                ORDER BY source, snapshot_time DESC
                """;
        return jdbc.query(sql, orderbookRowMapper(), symbol);
    }

    private RowMapper<OrderBookDto> orderbookRowMapper() {
        return (rs, rowNum) -> {
            OrderBookDto dto   = new OrderBookDto();
            dto.snapshotTime   = rs.getTimestamp("snapshot_time").toInstant().toString();
            dto.symbol         = rs.getString("symbol");
            dto.source         = rs.getString("source");
            dto.bids           = parseJsonb(rs.getObject("bids"));
            dto.asks           = parseJsonb(rs.getObject("asks"));
            return dto;
        };
    }

    private List<PriceLevelDto> parseJsonb(Object pgObj) {
        try {
            String json = pgObj instanceof PGobject po ? po.getValue() : pgObj.toString();
            return mapper.readValue(json, new TypeReference<List<PriceLevelDto>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}
