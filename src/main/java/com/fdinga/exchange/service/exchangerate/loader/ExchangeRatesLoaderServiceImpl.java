package com.fdinga.exchange.service.exchangerate.loader;

import com.fdinga.exchange.client.ecb.ECBEuroExchangeRateClient;
import com.fdinga.exchange.client.ecb.schema.stub.DateCube;
import com.fdinga.exchange.service.exchangerate.DailyExchangeRatesCache;
import com.fdinga.exchange.service.exchangerate.ExchangeRate;
import com.fdinga.exchange.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service designed to periodically load the current exchange rates and the exchange rates history (90 days) and store
 * them in-memory.
 *
 * @author Florin Dinga
 */
@Service
@PropertySource("classpath:application.properties")
public class ExchangeRatesLoaderServiceImpl implements ExchangeRatesLoaderService {

    @Autowired
    private ECBEuroExchangeRateClient ecbEuroExchangeRateClient;

    @Autowired
    private DailyExchangeRatesCache dailyExchangeRatesCache;

    /**
     * Loads the 90 days exchange rates history. It executes asynchronously after the app startup
     */
    @Async("asyncExecutor")
    public void loadExchangeRates() {
        List<DateCube> exchangeRateDateCubes = ecbEuroExchangeRateClient.getEuroExchangeRates();
        exchangeRateDateCubes.forEach(this::storeDailyExchangeRate);
    }

    /**
     * Updates the current exchange rates. The method executes based on the 'scheduler.exchangerate' cron expression
     * defined in the application properties.
     */
    @Scheduled(cron = "${scheduler.exchangerate}")
    public void updateCurrentDateExchangeRates() {
        DateCube exchangeRateDateCube = ecbEuroExchangeRateClient.getCurrentEuroExchangeRate();

        if (exchangeRateDateCube != null) {
            storeDailyExchangeRate(exchangeRateDateCube);
        }
    }

    private void storeDailyExchangeRate(DateCube exchangeRateDateCube) {
        List<ExchangeRate> dailyExchangeRates = exchangeRateDateCube.getCube()
                .stream()
                .map(currencyCube -> new ExchangeRate(currencyCube.getCurrency(), currencyCube.getRate()))
                .collect(Collectors.toList());

        dailyExchangeRatesCache.put(DateUtil.dateFromXmlGregorianCalendar(exchangeRateDateCube.getTime()),
                                    dailyExchangeRates);
    }
}
