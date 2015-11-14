package com.fdinga.exchange.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.springframework.util.Assert.notNull;

/**
 * @author Florin Dinga
 */
@Slf4j
public final class DateUtil {

    private DateUtil() {
    }

    public static XMLGregorianCalendar xmlGregorianCalendarFromDate(Date date) {
        notNull(date);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);

        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Data type configuration exception", e);
        }
    }
}
