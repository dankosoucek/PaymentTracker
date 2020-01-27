package com.dankosoucek.model;

public class Payment {
    private final String currency;
    private final double amount;

    public Payment(String currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }
}
