package com.binance.api.model;

public class TradeDto {
    public String tradeTime;   // ISO-8601
    public String symbol;
    public String source;
    public long   tradeId;
    public double price;
    public double quantity;
    public String side;        // "BUY" or "SELL"
    public long   latencyMs;
}
