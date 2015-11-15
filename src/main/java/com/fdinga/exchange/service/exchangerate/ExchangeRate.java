package com.fdinga.exchange.service.exchangerate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Structure designed to store the exchange rate with a target currency
 *
 * @author Florin Dinga
 */
@Getter
@EqualsAndHashCode
@ToString
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 7276266187579520605L;

    private String targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(String targetCurrency, BigDecimal rate) {
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
