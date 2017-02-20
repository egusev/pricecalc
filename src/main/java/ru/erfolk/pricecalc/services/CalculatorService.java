package ru.erfolk.pricecalc.services;

import java.math.BigDecimal;

/**
 * Created by eugene on 20.02.17.
 */
public interface CalculatorService {
    Calculator calculate(String isin, BigDecimal value, BigDecimal volatility);
}
