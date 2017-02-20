package ru.erfolk.pricecalc.services.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.erfolk.pricecalc.services.Calculator;

import java.math.BigDecimal;

/**
 * Created by eugene on 20.02.17.
 */
@Data
@AllArgsConstructor
public class CalculatorImpl implements Calculator {

    private String isin;

    private BigDecimal value;

    private BigDecimal volatility;

    @Override
    public BigDecimal calculate() {
        return BigDecimal.ZERO;
    }
}
