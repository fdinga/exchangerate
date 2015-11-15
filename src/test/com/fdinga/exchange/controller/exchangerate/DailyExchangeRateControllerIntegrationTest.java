package com.fdinga.exchange.controller.exchangerate;

import com.fdinga.exchange.ExchangeRateApplication;
import com.fdinga.exchange.config.ApplicationConfig;
import com.fdinga.exchange.controller.converter.StringToLocalDateConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author Florin Dinga
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExchangeRateApplication.class, ApplicationConfig.class})
@WebAppConfiguration
@TestPropertySource(properties = { "scheduler.exchangerate = * * * * * *"})
public class DailyExchangeRateControllerIntegrationTest {

    private static final String EXCHANGE_RATE_API_PATTERN = "/v1/exchange/%s/date/%s";
    private static final String TARGET_CURRENCY_CODE = "USD";
    private static final int NUMBER_OF_TARGET_CURRENCIES = 31;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetDailyExchangeRatesWithInvalidDateFormatReturnsBadRequest() throws Exception {
        this.mockMvc.perform(get(String.format("/v1/exchange/%s/date/10112015",
            DailyExchangeRateController.EURO_CURRENCY_CODE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyExchangeRatesWithDateOlderThan90DaysReturnsBadRequest() throws Exception {
        LocalDate pastDate = LocalDate.now().minusDays(91);
        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN, DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               pastDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyExchangeRatesWithDateInTheFutureReturnsBadRequest() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN, DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               futureDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyExchangeRatesWithCurrentDateReturnsOK() throws Exception {
        LocalDate currentDate = LocalDate.now();
        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN, DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               currentDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDailyExchangeRatesWith90DaysAgoDateReturnsOK() throws Exception {
        LocalDate ninetyDaysAgo = LocalDate.now().minusDays(90);
        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN, DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               ninetyDaysAgo.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetDailyExchangeRatesWithDayOfWeekReturnsOKWithContent() throws Exception {
        LocalDate dayOfWeek = getDayOfWeek();

        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN, DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               dayOfWeek.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(NUMBER_OF_TARGET_CURRENCIES)))
                .andExpect(jsonPath("$[0].targetCurrency").value(notNullValue()))
                .andExpect(jsonPath("$[0].targetCurrency", is(TARGET_CURRENCY_CODE)))
                .andExpect(jsonPath("$[0].rate").value(notNullValue()))
                .andExpect(jsonPath("$[0].rate", isA(Double.class)));

    }

    @Test
    public void testGetDailyExchangeRatesWithInvalidTargetCurrencyReturnsBadRequest() throws Exception {
        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN + "?targetCurrency=%s",
                                               DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               LocalDate.now().format(StringToLocalDateConverter.DATE_FORMATTER),
                                               "us")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyExchangeRatesWithTargetCurrencyAndDayOfWeekReturnsOKWithContent() throws Exception {
        LocalDate dayOfWeek = getDayOfWeek();

        this.mockMvc.perform(get(String.format(EXCHANGE_RATE_API_PATTERN + "?targetCurrency=%s",
                                               DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               dayOfWeek.format(StringToLocalDateConverter.DATE_FORMATTER),
                                               TARGET_CURRENCY_CODE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].targetCurrency").value(notNullValue()))
                .andExpect(jsonPath("$[0].targetCurrency", is(TARGET_CURRENCY_CODE)))
                .andExpect(jsonPath("$[0].rate").value(notNullValue()))
                .andExpect(jsonPath("$[0].rate", isA(Double.class)));

    }

    private LocalDate getDayOfWeek() {
        LocalDate currentDate = LocalDate.now();

        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            currentDate = currentDate.minusDays(2);
        }

        return currentDate;
    }
}
