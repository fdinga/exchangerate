package com.fdinga.exchange.client.ecb;


import com.fdinga.exchange.client.ecb.schema.stub.Cube;
import com.fdinga.exchange.client.ecb.schema.stub.CurrencyCube;
import com.fdinga.exchange.client.ecb.schema.stub.DateCube;
import com.fdinga.exchange.client.ecb.schema.stub.Envelope;
import com.fdinga.exchange.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


/**
 * @author Florin Dinga
 */
@RunWith(MockitoJUnitRunner.class)
public class ECBEuroExchangeRateClientTest {

    private static final String ECB_SERVICE_URL = "http://www.ecb.europa.eu/";
    private static final String TARGET_CURRENCY = "USD";
    private static final BigDecimal RATE = new BigDecimal("1.0764");
    private static final LocalDate CURRENT_DATE = LocalDate.now();

    @InjectMocks
    private ECBEuroExchangeRateClient ecbEuroExchangeRateClient;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(ecbEuroExchangeRateClient, "ecbServiceUrl", ECB_SERVICE_URL);
    }


    @Test
    public void testGetEuroExchangeRatesWithNullReturnedEnvelope() {
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_HISTORY_90_DAYS_XML,
                                       Envelope.class)).thenReturn(null);

        List<DateCube> exchangeRates = ecbEuroExchangeRateClient.getEuroExchangeRates();

        assertNotNull(exchangeRates);
        assertTrue(exchangeRates.isEmpty());
    }

    @Test
    public void testGetEuroExchangeRatesWithNullReturnedCube() {
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_HISTORY_90_DAYS_XML,
                                       Envelope.class)).thenReturn(new Envelope());

        List<DateCube> exchangeRates = ecbEuroExchangeRateClient.getEuroExchangeRates();

        assertNotNull(exchangeRates);
        assertTrue(exchangeRates.isEmpty());
    }

    @Test
    public void testGetEuroExchangeRatesWithNullReturnedDateCube() {
        Envelope envelope = new Envelope();
        envelope.setCube(new Cube());
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_HISTORY_90_DAYS_XML,
                                       Envelope.class)).thenReturn(envelope);

        List<DateCube> exchangeRates = ecbEuroExchangeRateClient.getEuroExchangeRates();

        assertNotNull(exchangeRates);
        assertTrue(exchangeRates.isEmpty());
    }

    @Test
    public void testGetEuroExchangeRates() {
        Envelope envelope = setupEnvelope();
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_HISTORY_90_DAYS_XML,
                                       Envelope.class)).thenReturn(envelope);

        List<DateCube> exchangeRates = ecbEuroExchangeRateClient.getEuroExchangeRates();

        assertNotNull(exchangeRates);
        assertEquals(1, exchangeRates.size());
        assertEquals(envelope.getCube().getCube(), exchangeRates);
    }

    @Test
    public void testGetCurrentEuroExchangeRateWithNullReturnedEnvelope() {
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_DAILY_XML,
                                       Envelope.class)).thenReturn(null);

        DateCube currentExchangeRate = ecbEuroExchangeRateClient.getCurrentEuroExchangeRate();
        assertNull(currentExchangeRate);
    }

    @Test
    public void testGetCurrentEuroExchangeRateWithNullReturnedCube() {
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_DAILY_XML,
                                       Envelope.class)).thenReturn(new Envelope());

        DateCube currentExchangeRate = ecbEuroExchangeRateClient.getCurrentEuroExchangeRate();
        assertNull(currentExchangeRate);
    }

    @Test
    public void testGetCurrentEuroExchangeRateWithNullReturnedDateCubes() {
        Envelope envelope = new Envelope();
        envelope.setCube(new Cube());
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_DAILY_XML,
                                       Envelope.class)).thenReturn(envelope);

        DateCube currentExchangeRate = ecbEuroExchangeRateClient.getCurrentEuroExchangeRate();
        assertNull(currentExchangeRate);
    }

    @Test
    public void testGetCurrentEuroExchangeRateWithEmptyReturnedDateCubes() {
        Envelope envelope = setupEnvelope();
        envelope.getCube().getCube().clear();
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_DAILY_XML,
                                       Envelope.class)).thenReturn(envelope);

        DateCube currentExchangeRate = ecbEuroExchangeRateClient.getCurrentEuroExchangeRate();
        assertNull(currentExchangeRate);
    }

    @Test
    public void testGetCurrentEuroExchangeRate() {
        Envelope envelope = setupEnvelope();
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_DAILY_XML,
                                       Envelope.class)).thenReturn(envelope);

        DateCube currentExchangeRate = ecbEuroExchangeRateClient.getCurrentEuroExchangeRate();

        assertNotNull(currentExchangeRate);
        assertEquals(DateUtil.xmlGregorianCalendarFromDate(CURRENT_DATE), currentExchangeRate.getTime());

        assertNotNull(currentExchangeRate.getCube());
        assertEquals(1, currentExchangeRate.getCube().size());
        CurrencyCube currencyCube = currentExchangeRate.getCube().get(0);
        assertEquals(TARGET_CURRENCY, currencyCube.getCurrency());
        assertEquals(RATE, currencyCube.getRate());
    }

    private Envelope setupEnvelope() {
        Envelope envelope = new Envelope();
        Cube cube = new Cube();
        envelope.setCube(cube);

        DateCube dateCube = new DateCube();
        dateCube.setTime(DateUtil.xmlGregorianCalendarFromDate(CURRENT_DATE));

        CurrencyCube currencyCube = new CurrencyCube();
        currencyCube.setCurrency(TARGET_CURRENCY);
        currencyCube.setRate(RATE);
        dateCube.getCube().add(currencyCube);
        cube.getCube().add(dateCube);

        return envelope;
    }

}