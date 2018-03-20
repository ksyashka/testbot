package com.ksenia.testbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class Currency {
    @JsonProperty("r030")
    private int id;
    @JsonProperty("txt")
    private String currencyName;
    private double rate;
    @JsonProperty("cc")
    private String currencyCode;
    @JsonProperty("exchangedate")
    private String exchangeDate;

    public Currency() {
    }

    public Currency(int r030, String txt, double rate, String cc, String exchangedate) {
        this.id = r030;
        this.currencyName = txt;
        this.rate = rate;
        this.currencyCode = cc;
        this.exchangeDate = exchangedate;
    }

    public Currency(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return id == currency.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}