package com.binance.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trades")
public class Trade {

    @EmbeddedId
    public TradeId id;

    @Column(name = "price")          public double price;
    @Column(name = "quantity")       public double quantity;
    @Column(name = "side")           public String side;
    @Column(name = "latency_ms")     public long   latencyMs;
}
