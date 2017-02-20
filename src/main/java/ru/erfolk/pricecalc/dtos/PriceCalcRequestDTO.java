package ru.erfolk.pricecalc.dtos;

/**
 * Created by eugene on 20.02.17.
 */

import lombok.Data;

@Data
public class PriceCalcRequestDTO {
    private String isin;

    private Double value;

    private Double volatility;
}
