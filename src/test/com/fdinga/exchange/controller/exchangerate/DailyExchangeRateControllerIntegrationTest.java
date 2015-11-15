package com.fdinga.exchange.controller.exchangerate;

import com.fdinga.exchange.ExchangeRateApplication;
import com.fdinga.exchange.config.ApplicationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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
        this.mockMvc.perform(get(String.format("/v1/exchange/%s/date/%s", DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               pastDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyExchangeRatesWithDateInTheFutureReturnsBadRequest() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        this.mockMvc.perform(get(String.format("/v1/exchange/%s/date/%s", DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               futureDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyExchangeRatesWithCurrentDateReturnsOK() throws Exception {
        LocalDate currentDate = LocalDate.now();
        this.mockMvc.perform(get(String.format("/v1/exchange/%s/date/%s", DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               currentDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDailyExchangeRatesWith90DaysAgoDateReturnsOKWithNoContent() throws Exception {
        LocalDate ninetyDaysAgo = LocalDate.now().minusDays(90);
        this.mockMvc.perform(get(String.format("/v1/exchange/%s/date/%s", DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               ninetyDaysAgo.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    public void testGetDailyExchangeRatesWithDayOfWeekReturnsOKWithContent() throws Exception {
        LocalDate currentDate = LocalDate.now();

        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            currentDate = currentDate.minusDays(2);
        }
        //sleep until the scheduler runs and loads the exchange rates in memory
        Thread.sleep(2000);
        this.mockMvc.perform(get(String.format("/v1/exchange/%s/date/%s", DailyExchangeRateController.EURO_CURRENCY_CODE,
                                               currentDate.format(StringToLocalDateConverter.DATE_FORMATTER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(31)))
                .andExpect(jsonPath("$[0].targetCurrency").value(notNullValue()))
                .andExpect(jsonPath("$[0].targetCurrency", is("USD")))
                .andExpect(jsonPath("$[0].rate").value(notNullValue()))
                .andExpect(jsonPath("$[0].rate", isA(Double.class)));

    }
}
