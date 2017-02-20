package ru.erfolk.pricecalc.services.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.erfolk.pricecalc.services.Calculator;
import ru.erfolk.pricecalc.services.CalculatorService;

import java.math.BigDecimal;

/**
 * Created by eugene on 20.02.17.
 */
@Service
@Profile({"test"})
public class CalculatorServiceTestImpl implements CalculatorService {
    @Override
    public Calculator calculate(String isin, BigDecimal value, BigDecimal volatility) {
        return new CalculatorTestImpl(isin, value, volatility);
    }
}
