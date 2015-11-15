package com.fdinga.exchange.controller.converter;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Florin Dinga
 */
public class StringToLocalDateConverterTest {

    private StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter();

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullInput() {
        stringToLocalDateConverter.convert(null);
    }

    @Test(expected = DateTimeParseException.class)
    public void testConvertWithWrongFormat() {
        stringToLocalDateConverter.convert("10/10/2015");
    }

    @Test
    public void testConvertWithCorrectFormat() {
        LocalDate localDate = stringToLocalDateConverter.convert("10-11-2015");

        assertNotNull(localDate);
        assertEquals(LocalDate.of(2015, 11, 10), localDate);
    }
}