package com.fdinga.exchange.service.exchangerate;


import com.fdinga.exchange.service.exchangerate.exception.ExchangeRateValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Florin Dinga
 */
@RunWith(MockitoJUnitRunner.class)
public class DailyExchangeRateServiceImplTest {

    private static final LocalDate DATE = LocalDate.now();
    private static final String TARGET_CURRENCY_CODE = "USD";

    @InjectMocks
    private DailyExchangeRateServiceImpl dailyExchangeRateService;

    @Mock
    private DailyExchangeRatesCache dailyExchangeRatesCache;


    @Test
    public void testGetDailyExchangeRatesWithNoCacheEntry() {
        when(dailyExchangeRatesCache.get(DATE)).thenReturn(null);

        List<ExchangeRate> dailyExchangeRates = dailyExchangeRateService.getEuroDailyExchangeRates(DATE, null);

        assertNotNull(dailyExchangeRates);
        assertTrue(dailyExchangeRates.isEmpty());
    }

    @Test
    public void testGetDailyExchangeRatesWithTargetCurrencyAndNoCacheEntry() {
        when(dailyExchangeRatesCache.get(DATE)).thenReturn(null);

        List<ExchangeRate> dailyExchangeRates = dailyExchangeRateService.getEuroDailyExchangeRates(DATE,
            Currency.getInstance(TARGET_CURRENCY_CODE));

        assertNotNull(dailyExchangeRates);
        assertTrue(dailyExchangeRates.isEmpty());
    }

    @Test
    public void testGetDailyExchangeRatesWithCurrentDate() {
        List<ExchangeRate> exchangeRates = setupExchangeRates();
        when(dailyExchangeRatesCache.get(DATE)).thenReturn(exchangeRates);

        List<ExchangeRate> dailyExchangeRates = dailyExchangeRateService.getEuroDailyExchangeRates(DATE, null);

        assertNotNull(dailyExchangeRates);
        assertEquals(exchangeRates, dailyExchangeRates);
    }

    @Test
    public void testGetDailyExchangeRatesWithCurrentDateAndTargetCurrency() {
        List<ExchangeRate> exchangeRates = setupExchangeRates();
        when(dailyExchangeRatesCache.get(DATE)).thenReturn(exchangeRates);

        List<ExchangeRate> dailyExchangeRates = dailyExchangeRateService.getEuroDailyExchangeRates(DATE,
            Currency.getInstance(TARGET_CURRENCY_CODE));

        assertNotNull(dailyExchangeRates);
        assertEquals(1, dailyExchangeRates.size());
        assertEquals(TARGET_CURRENCY_CODE, dailyExchangeRates.get(0).getTargetCurrency());
    }

    @Test
    public void testGetDailyExchangeRatesWithPastDate90DaysBefore() {
        LocalDate pastDate = DATE.minusDays(DailyExchangeRateServiceImpl.MAX_PAST_DAYS);
        List<ExchangeRate> exchangeRates = setupExchangeRates();
        when(dailyExchangeRatesCache.get(pastDate)).thenReturn(exchangeRates);

        List<ExchangeRate> dailyExchangeRates = dailyExchangeRateService.getEuroDailyExchangeRates(
                pastDate, null);

        assertNotNull(dailyExchangeRates);
        assertEquals(exchangeRates, dailyExchangeRates);
    }

    @Test(expected = ExchangeRateValidationException.class)
    public void testGetDailyExchangeRatesWithPastDateMoreThan90DaysBefore() {
        LocalDate pastDate = DATE.minusDays(DailyExchangeRateServiceImpl.MAX_PAST_DAYS + 1);
        List<ExchangeRate> exchangeRates = setupExchangeRates();
        when(dailyExchangeRatesCache.get(pastDate)).thenReturn(exchangeRates);

        dailyExchangeRateService.getEuroDailyExchangeRates(pastDate, null);
    }

    @Test(expected = ExchangeRateValidationException.class)
    public void testGetDailyExchangeRatesWithFutureDateMoreThan90DaysBefore() {
        LocalDate futureDate = DATE.plusDays(1);
        List<ExchangeRate> exchangeRates = setupExchangeRates();
        when(dailyExchangeRatesCache.get(futureDate)).thenReturn(exchangeRates);

        dailyExchangeRateService.getEuroDailyExchangeRates(futureDate, null);
    }

    private List<ExchangeRate> setupExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(new ExchangeRate(TARGET_CURRENCY_CODE, new BigDecimal("1.0234")));
        exchangeRates.add(new ExchangeRate("DKK", new BigDecimal("7.4602")));
        return exchangeRates;
    }

}