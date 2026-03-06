package com.binance.api.model;

import java.util.List;

public class OrderBookDto {
    public String             source;
    public String             symbol;
    public String             snapshotTime;  // ISO-8601
    public List<PriceLevelDto> bids;
    public List<PriceLevelDto> asks;
}
