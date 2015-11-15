package com.fdinga.exchange.service.exchangerate;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

/**
 * @author Florin Dinga
 */
@Validated
public interface DailyExchangeRateService {

    List<ExchangeRate> getEuroDailyExchangeRates(@NotNull(message = "Date should not be null") LocalDate date,
                                                 Currency targetCurrency);
}
