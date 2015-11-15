package com.fdinga.exchange.service.exchangerate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Florin Dinga
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExchangeRateException extends RuntimeException {

    private static final long serialVersionUID = 3013569184400514315L;

    public ExchangeRateException(String message) {
        super(message);
    }
}
