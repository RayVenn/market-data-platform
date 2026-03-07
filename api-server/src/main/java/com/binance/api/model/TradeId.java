package com.binance.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class TradeId implements Serializable {

    @Column(name = "symbol") public String symbol;
    @Column(name = "source") public String source;
    @Column(name = "trade_id") public long tradeId;
    @Column(name = "trade_time") public Instant tradeTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeId t)) return false;
        return tradeId == t.tradeId
                && Objects.equals(symbol, t.symbol)
                && Objects.equals(source, t.source)
                && Objects.equals(tradeTime, t.tradeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, source, tradeId, tradeTime);
    }
}
