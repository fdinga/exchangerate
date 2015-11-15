package com.fdinga.exchange.controller.exchangerate;

import com.fdinga.exchange.service.exchangerate.DailyExchangeRateService;
import com.fdinga.exchange.service.exchangerate.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
public class DailyExchangeRateController {

    public static final String EURO_CURRENCY_CODE = "eur";
    public static final String DATE = "date";

    @Autowired
    private DailyExchangeRateService dailyExchangeRateService;

    @RequestMapping("/v1/exchange/" + EURO_CURRENCY_CODE + "/date/{date}")
    @ResponseBody
    public List<ExchangeRate> getForeignExchangeRates(@PathVariable(DATE) LocalDate date) {
        log.debug("GET foreign exchange rate for currency '{}' and date '{}'", EURO_CURRENCY_CODE, date);
        return dailyExchangeRateService.getEuroDailyExchangeRates(date);
    }
}
