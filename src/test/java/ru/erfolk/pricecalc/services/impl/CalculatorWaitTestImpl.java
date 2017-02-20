package ru.erfolk.pricecalc.services.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.erfolk.pricecalc.services.Calculator;

import java.math.BigDecimal;

/**
 * Created by eugene on 20.02.17.
 */
@Data
@AllArgsConstructor
@Slf4j
public class CalculatorWaitTestImpl implements Calculator {

    private String isin;

    private BigDecimal value;

    private BigDecimal volatility;

    @Override
    public BigDecimal calculate() {
        try {
            log.warn("sleep for {} s", volatility.longValue());
            Thread.sleep(volatility.longValue());
            return value;
        } catch (InterruptedException e) {
            return null;
        }
    }
}
