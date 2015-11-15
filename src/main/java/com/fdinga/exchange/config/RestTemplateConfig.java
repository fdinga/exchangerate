package com.fdinga.exchange.config;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Florin Dinga
 */
@Configuration
@PropertySource("classpath:application.properties")
public class RestTemplateConfig {

    @Value("${httpclient.connection.timeout.millis}")
    private Integer connectionTimeoutMillis;

    @Value("${httpclient.read.timeout.millis}")
    private Integer readTimeoutMills;

    @Value("${httpclient.max.connections.route}")
    private Integer maxConnectionsPerRoute;

    @Value("${httpclient.max.connections.total}")
    private Integer maxTotalConnections;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                buildHttpClient());
        requestFactory.setConnectTimeout(connectionTimeoutMillis);
        requestFactory.setReadTimeout(readTimeoutMills);

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private CloseableHttpClient buildHttpClient() {
        HttpClientBuilder builder = HttpClients.custom();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connectionManager.setMaxTotal(maxTotalConnections);

        builder.setConnectionManager(connectionManager);
        return builder.build();
    }
}
