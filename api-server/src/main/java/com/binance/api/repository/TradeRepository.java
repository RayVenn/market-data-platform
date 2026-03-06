package com.binance.api.repository;

import com.binance.api.model.TradeDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class TradeRepository {

    private final JdbcTemplate jdbc;

    public TradeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<TradeDto> findRecent(String symbol, int limit) {
        String sql = """
                SELECT trade_time, symbol, source, trade_id, price, quantity, side, latency_ms
                FROM trades
                WHERE symbol = ?
                ORDER BY trade_time DESC
                LIMIT ?
                """;
        return jdbc.query(sql, tradeRowMapper(), symbol, limit);
    }

    private @NonNull RowMapper<TradeDto> tradeRowMapper() {
        return (@NonNull ResultSet rs, int rowNum) -> {
            TradeDto t  = new TradeDto();
            t.tradeTime = rs.getTimestamp("trade_time").toInstant().toString();
            t.symbol    = rs.getString("symbol");
            t.source    = rs.getString("source");
            t.tradeId   = rs.getLong("trade_id");
            t.price     = rs.getDouble("price");
            t.quantity  = rs.getDouble("quantity");
            t.side      = rs.getString("side");
            t.latencyMs = rs.getLong("latency_ms");
            return t;
        };
    }
}
