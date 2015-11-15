package com.fdinga.exchange.controller.exchangerate;

import com.fdinga.exchange.service.exchangerate.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class DailyExchangeRateController {

    public static final String EURO_CURRENCY_CODE = "EUR";
    public static final String DATE = "date";

    @RequestMapping("/v1/exchange/" + EURO_CURRENCY_CODE + "/date/{date}")
    @ResponseBody
    public List<ExchangeRate> getForeignExchangeRates(@PathVariable(DATE) String dateStr) {
        log.debug("GET foreign exchange rate for currency '{}' and date '{}'", EURO_CURRENCY_CODE, dateStr);
        return null;
    }
}
