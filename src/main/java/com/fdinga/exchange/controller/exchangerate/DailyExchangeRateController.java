package com.fdinga.exchange.controller.exchangerate;

import com.fdinga.exchange.service.exchangerate.DailyExchangeRates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class DailyExchangeRateController {

    public static final String EURO_CURRENCY_CODE = "EUR";
    public static final String DATE = "date";

    @RequestMapping("/v1/exchange/" + EURO_CURRENCY_CODE + "/date/{date}")
    @ResponseBody
    public  DailyExchangeRates getForeignExchangeRates(@PathVariable(DATE) String date) {
        log.debug("GET foreign exchange rate for currency '{}' and date '{}'", EURO_CURRENCY_CODE, date);
        return null;
    }
}
