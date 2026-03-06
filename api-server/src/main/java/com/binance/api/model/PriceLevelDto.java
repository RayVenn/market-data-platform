package com.binance.api.model;

public class PriceLevelDto {
    public double price;
    public double quantity;

    public PriceLevelDto() {}

    public PriceLevelDto(double price, double quantity) {
        this.price    = price;
        this.quantity = quantity;
    }
}
