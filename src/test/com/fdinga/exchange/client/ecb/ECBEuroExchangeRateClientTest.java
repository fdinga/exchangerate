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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


/**
 * @author Florin Dinga
 */
@RunWith(MockitoJUnitRunner.class)
public class ECBEuroExchangeRateClientTest {

    public static final String ECB_SERVICE_URL = "http://www.ecb.europa.eu/";
    public static final String TARGET_CURRENCY = "USD";
    public static final BigDecimal RATE = new BigDecimal("1.0764");
    public static final Date CURRENT_DATE = new Date();

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
    public void testGetEuroExchangeRates() {
        Envelope envelope = setupEnvelope();
        when(restTemplate.getForObject(ECB_SERVICE_URL + ECBEuroExchangeRateClient.EURO_FX_REF_HISTORY_90_DAYS_XML,
                                       Envelope.class)).thenReturn(envelope);

        List<DateCube> exchangeRates = ecbEuroExchangeRateClient.getEuroExchangeRates();

        assertNotNull(exchangeRates);
        assertEquals(1, exchangeRates.size());
        assertEquals(envelope.getCube().getCube(), exchangeRates);
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