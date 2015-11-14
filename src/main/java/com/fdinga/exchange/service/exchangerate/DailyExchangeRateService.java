package com.fdinga.exchange.service.exchangerate;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Florin Dinga
 */
@Validated
public interface DailyExchangeRateService {

    DailyExchangeRates getEuroDailyExchangeRates(@NotNull(message = "Date should not bn null") Date date);
}
