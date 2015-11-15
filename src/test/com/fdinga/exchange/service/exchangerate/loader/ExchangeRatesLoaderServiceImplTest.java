package com.fdinga.exchange.service.exchangerate.loader;


import com.fdinga.exchange.client.ecb.ECBEuroExchangeRateClient;
import com.fdinga.exchange.client.ecb.schema.stub.CurrencyCube;
import com.fdinga.exchange.client.ecb.schema.stub.DateCube;
import com.fdinga.exchange.service.exchangerate.DailyExchangeRatesCache;
import com.fdinga.exchange.service.exchangerate.ExchangeRate;
import com.fdinga.exchange.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Florin Dinga
 */
@RunWith(MockitoJUnitRunner.class)
public class ExchangeRatesLoaderServiceImplTest {

    private static final String TARGET_CURRENCY = "USD";
    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final LocalDate SECOND_DATE = CURRENT_DATE.plusDays(1);

    private static final BigDecimal RATE = new BigDecimal("1.0234");
    private static final BigDecimal SECOND_RATE = new BigDecimal("1.0212");

    @InjectMocks
    private ExchangeRatesLoaderServiceImpl exchangeRatesLoaderService;

    @Mock
    private ECBEuroExchangeRateClient ecbEuroExchangeRateClient;

    @Mock
    private DailyExchangeRatesCache dailyExchangeRatesCache;

    @Test
    public void testLoadExchangeRatesWithNoExchangeRatesFound() {
        when(ecbEuroExchangeRateClient.getEuroExchangeRates()).thenReturn(new ArrayList<>());
        exchangeRatesLoaderService.loadExchangeRates();

        verifyNoMoreInteractions(dailyExchangeRatesCache);
    }

    @Test
    public void testLoadExchangeRates() {
        List<DateCube> exchangeRateDateCubes = setupExchangeRateDateCubes();
        when(ecbEuroExchangeRateClient.getEuroExchangeRates()).thenReturn(exchangeRateDateCubes);

        exchangeRatesLoaderService.loadExchangeRates();

        List<ExchangeRate> dailyExchangeRates = new ArrayList<>();
        dailyExchangeRates.add(new ExchangeRate(TARGET_CURRENCY, RATE));
        verify(dailyExchangeRatesCache).put(CURRENT_DATE, dailyExchangeRates);

        List<ExchangeRate> secondDailyExchangeRates = new ArrayList<>();
        secondDailyExchangeRates.add(new ExchangeRate(TARGET_CURRENCY, SECOND_RATE));
        verify(dailyExchangeRatesCache).put(SECOND_DATE, secondDailyExchangeRates);

        verifyNoMoreInteractions(dailyExchangeRatesCache);
    }

    @Test
    public void testUpdateCurrentDateExchangeRatesWithNoExchangeRatesFound() {
        when(ecbEuroExchangeRateClient.getCurrentEuroExchangeRate()).thenReturn(null);
        exchangeRatesLoaderService.updateCurrentDateExchangeRates();

        verifyNoMoreInteractions(dailyExchangeRatesCache);
    }

    @Test
    public void testUpdateCurrentDateExchangeRates() {
        DateCube currentDateCube = setupExchangeRateDateCubes().get(0);
        when(ecbEuroExchangeRateClient.getCurrentEuroExchangeRate()).thenReturn(currentDateCube);

        exchangeRatesLoaderService.updateCurrentDateExchangeRates();

        List<ExchangeRate> dailyExchangeRates = new ArrayList<>();
        dailyExchangeRates.add(new ExchangeRate(TARGET_CURRENCY, RATE));
        verify(dailyExchangeRatesCache).put(CURRENT_DATE, dailyExchangeRates);

        verifyNoMoreInteractions(dailyExchangeRatesCache);
    }

    private List<DateCube> setupExchangeRateDateCubes() {
        List<DateCube> exchangeRateDateCubes = new ArrayList<>();
        DateCube dateCube = new DateCube();
        dateCube.setTime(DateUtil.xmlGregorianCalendarFromDate(CURRENT_DATE));

        CurrencyCube currencyCube = new CurrencyCube();
        currencyCube.setCurrency(TARGET_CURRENCY);
        currencyCube.setRate(RATE);
        dateCube.getCube().add(currencyCube);
        exchangeRateDateCubes.add(dateCube);

        DateCube secondDateCube = new DateCube();
        secondDateCube.setTime(DateUtil.xmlGregorianCalendarFromDate(SECOND_DATE));

        CurrencyCube secondCurrencyCube = new CurrencyCube();
        secondCurrencyCube.setCurrency(TARGET_CURRENCY);
        secondCurrencyCube.setRate(SECOND_RATE);
        secondDateCube.getCube().add(secondCurrencyCube);
        exchangeRateDateCubes.add(secondDateCube);
        return exchangeRateDateCubes;
    }
}