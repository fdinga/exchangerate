package com.fdinga.exchange.service.exchangerate;

import com.fdinga.exchange.service.exchangerate.loader.ExchangeRatesLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service designed to fetch the daily euro exchange rates from the local cache.
 * On post construct, it triggers the loading of the exchange rates history, asynchronously.
 *
 * @author Florin Dinga
 */
@Service
public class DailyExchangeRateServiceImpl implements DailyExchangeRateService {

    public static final Integer MAX_PAST_DAYS = 90;

    @Autowired
    private DailyExchangeRatesCache dailyExchangeRatesCache;

    @Autowired
    private ExchangeRatesLoaderService exchangeRatesLoaderService;

    @PostConstruct
    public void loadExchangeRatesHistory() {
        exchangeRatesLoaderService.loadExchangeRates();
    }

    @Override
    public List<ExchangeRate> getEuroDailyExchangeRates(LocalDate date) {
        LocalDate currentDate = LocalDate.now();

        if (date.isAfter(currentDate)) {
            throw new ExchangeRateException("Input date should not be in the future");
        }

        if (date.isBefore(currentDate.minusDays(MAX_PAST_DAYS))) {
            throw new ExchangeRateException(String.format("Input date should not be more than '%s' days in the past",
                                                          MAX_PAST_DAYS));
        }

        List<ExchangeRate> dailyExchangeRates = dailyExchangeRatesCache.get(date);
        return dailyExchangeRates != null ? dailyExchangeRates : new ArrayList<>();
    }
}
