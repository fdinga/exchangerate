package com.fdinga.exchange.service.exchangerate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Florin Dinga
 */
@Getter
@ToString
public class DailyExchangeRates implements Serializable {

    private static final long serialVersionUID = 4732748856960827523L;

    private String sourceCurrency;
    private Date date;

    @Getter(AccessLevel.NONE)
    private List<ExchangeRate> targetExchangeRates = new ArrayList<>();

    public DailyExchangeRates(String sourceCurrency, Date date) {
        this.sourceCurrency = sourceCurrency;
        this.date = date;
    }

    public void addTargetExchangeRate(ExchangeRate exchangeRate) {
        targetExchangeRates.add(exchangeRate);
    }

    public List<ExchangeRate> getTargetExchangeRates() {
        return new ArrayList<>(targetExchangeRates);
    }
}
