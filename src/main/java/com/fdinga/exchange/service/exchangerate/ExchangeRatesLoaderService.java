package com.fdinga.exchange.service.exchangerate;

import com.fdinga.exchange.client.ecb.ECBEuroExchangeRateClient;
import com.fdinga.exchange.client.ecb.schema.stub.DateCube;
import com.fdinga.exchange.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service designed to periodically load the exchange rates and store them in memory
 *
 * @author Florin Dinga
 */
@Service
@PropertySource("classpath:application.properties")
public class ExchangeRatesLoaderService {

    @Autowired
    private ECBEuroExchangeRateClient ecbEuroExchangeRateClient;

    @Autowired
    private DailyExchangeRatesCache dailyExchangeRatesCache;

    /**
     * The method executes based on the 'scheduler.exchangerate' cron expression defined in the application properties.
     */
    @Scheduled(cron = "${scheduler.exchangerate}")
    public void loadExchangeRates() {
        List<DateCube> exchangeRateDateCubes = ecbEuroExchangeRateClient.getEuroExchangeRates();

        for (DateCube exchangeRateDateCube : exchangeRateDateCubes) {
            List<ExchangeRate> dailyExchangeRates = exchangeRateDateCube.getCube()
                    .stream()
                    .map(currencyCube -> new ExchangeRate(currencyCube.getCurrency(), currencyCube.getRate()))
                    .collect(Collectors.toList());

            dailyExchangeRatesCache.put(DateUtil.dateFromXmlGregorianCalendar(exchangeRateDateCube.getTime()),
                                        dailyExchangeRates);
        }
    }
}
