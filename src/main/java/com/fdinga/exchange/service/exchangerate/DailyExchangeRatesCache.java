package com.fdinga.exchange.service.exchangerate;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * In-memory cache for storing the daily exchange rates
 *
 * @author Florin Dinga
 */
@Component
public class DailyExchangeRatesCache {

    private Map<LocalDate, List<ExchangeRate>> dailyExchangeRatesMap = new HashMap<>();

    public void put(LocalDate date, List<ExchangeRate> dailyExchangeRates) {
        notNull(date, "Date should not be null");
        dailyExchangeRatesMap.put(date, dailyExchangeRates);
    }

    public List<ExchangeRate> get(LocalDate date) {
        notNull(date, "Date should not be null");
        return dailyExchangeRatesMap.get(date);
    }
}
