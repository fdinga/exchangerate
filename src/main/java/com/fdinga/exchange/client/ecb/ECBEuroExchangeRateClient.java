package com.fdinga.exchange.client.ecb;

import com.fdinga.exchange.client.ecb.schema.stub.DateCube;
import com.fdinga.exchange.client.ecb.schema.stub.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Client for the European central bank exchange rate service.
 *
 * @author Florin Dinga
 */
@Component
@PropertySource("classpath:application.properties")
public class ECBEuroExchangeRateClient {

    public static final String EURO_FX_REF_HISTORY_90_DAYS_XML = "stats/eurofxref/eurofxref-hist-90d.xml";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ecb.service.url}")
    private String ecbServiceUrl;

    /**
     * Fetch the list of the EUR foreign exchange rates for the last 90 days
     *
     * @return a list of DateCube, each element representing the exchange rates for one date
     */
    public List<DateCube> getEuroExchangeRates() {
        Envelope envelope =  restTemplate.getForObject(
                ecbServiceUrl + EURO_FX_REF_HISTORY_90_DAYS_XML, Envelope.class);

        return envelope != null ? envelope.getCube().getCube() : new ArrayList<>();
    }
}