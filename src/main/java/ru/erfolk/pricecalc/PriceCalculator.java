package ru.erfolk.pricecalc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by eugene on 20.02.17.
 */
@SpringBootApplication
public class PriceCalculator {

    @Value("${executor.core_pool_size}")
    private Integer CORE_POOL_SIZE;

    @Value("${executor.max_pool_size}")
    private Integer MAX_POOL_SIZE;

    @Value("${executor.wait_tasks_on_shutdown}")
    private Boolean WAIT_TASKS_ON_SHUTDOWN;

    @Bean
    public ThreadPoolExecutor taskExecutor() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    public static void main(String[] args) {
        SpringApplication.run(PriceCalculator.class, args);
    }
}
