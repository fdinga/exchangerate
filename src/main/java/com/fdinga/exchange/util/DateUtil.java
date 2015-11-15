package com.fdinga.exchange.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public static LocalDate dateFromXmlGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
        notNull(xmlGregorianCalendar, "Input xmlGregorianCalendar should not be null");
        return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }

    public static XMLGregorianCalendar xmlGregorianCalendarFromDate(LocalDate date) {
        notNull(date, "Input date should not be null");
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(Date.from(date.atStartOfDay(ZoneId.systemDefault() ).toInstant()));

        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Data type configuration exception", e);
        }
    }
}
