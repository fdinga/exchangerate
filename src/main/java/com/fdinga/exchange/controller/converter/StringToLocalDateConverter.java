package com.fdinga.exchange.controller.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.util.Assert.notNull;

/**
 * @author Florin Dinga
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    public static final String SUPPORTED_DATE_FORMAT = "dd-MM-yyyy";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(SUPPORTED_DATE_FORMAT);

    @Override
    public LocalDate convert(String dateStr) {
        notNull(dateStr, "Input date string should not be null");
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
}
