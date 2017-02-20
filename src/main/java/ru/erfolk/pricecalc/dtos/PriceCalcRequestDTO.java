package ru.erfolk.pricecalc.dtos;

/**
 * Created by eugene on 20.02.17.
 */

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PriceCalcRequestDTO {

    // according to the wiki it should be the 12 symbol length
    @NotNull
    @Pattern(regexp = "^[\\p{Alnum}]{12}$")
    private String isin;

    // don't know the valid values range. but still. let it be 0-100. can be removed without any issue
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal value;

    // don't know the valid values range. but still. let it be 0-10000. can be removed without any issue
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10000.0")
    private BigDecimal volatility;
}
