package com.fdinga.exchange.controller.exchangerate;

import com.fdinga.exchange.service.exchangerate.DailyExchangeRateService;
import com.fdinga.exchange.service.exchangerate.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Slf4j
@Controller
public class DailyExchangeRateController {

    public static final String EURO_CURRENCY_CODE = "eur";
    public static final String DATE = "date";
    public static final String TARGET_CURRENCY = "targetCurrency";

    @Autowired
    private DailyExchangeRateService dailyExchangeRateService;

    @RequestMapping("/v1/exchange/" + EURO_CURRENCY_CODE + "/date/{date}")
    @ResponseBody
    public List<ExchangeRate> getForeignExchangeRates(@PathVariable(DATE) LocalDate date,
        @RequestParam(value= TARGET_CURRENCY, required = false) Currency targetCurrency) {
        log.debug("GET foreign exchange rates for source currency '{}', date '{}' and target currency '{}'",
                  EURO_CURRENCY_CODE, date, targetCurrency);
        return dailyExchangeRateService.getEuroDailyExchangeRates(date, targetCurrency);
    }
}
