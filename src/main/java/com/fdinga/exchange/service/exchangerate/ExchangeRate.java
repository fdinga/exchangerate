package com.fdinga.exchange.service.exchangerate;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Florin Dinga
 */
@Getter
@ToString
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 7276266187579520605L;

    private String targetCurrencyCode;
    private BigDecimal rate;

    public ExchangeRate(String targetCurrencyCode, BigDecimal rate) {
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }
}
