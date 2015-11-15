package com.fdinga.exchange.config;

import com.fdinga.exchange.controller.converter.StringToLocalDateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Florin Dinga
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${scheduler.max.pool.size}")
    private Integer schedulerMaxPoolSize;

    @Value("${async.executor.max.pool.size}")
    private Integer asyncExecutorMaxPoolSize;

    @Value("${async.executor.queue.capacity}")
    private Integer asyncExecutorQueueCapacity;

    @Bean
    public ScheduledExecutorFactoryBean scheduledExecutorFactoryBean() {
        ScheduledExecutorFactoryBean scheduledExecutorFactoryBean = new ScheduledExecutorFactoryBean();
        scheduledExecutorFactoryBean.setThreadNamePrefix("scheduler-");
        scheduledExecutorFactoryBean.setPoolSize(schedulerMaxPoolSize);
        return scheduledExecutorFactoryBean;
    }

    @Bean(name="asyncExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(asyncExecutorMaxPoolSize);
        taskExecutor.setQueueCapacity(asyncExecutorMaxPoolSize);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public StringToLocalDateConverter stringToLocalDateConverter() {
        return new StringToLocalDateConverter();
    }
}
