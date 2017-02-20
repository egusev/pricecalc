package ru.erfolk.pricecalc.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eugene on 20.02.17.
 */
@Data
public class PriceCalcResponseDTO {

    private boolean success = true;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date date = null;

    // I'll prefer use the BigDecimal for currency, amounts etc
    // but the requirement says about the double format. ok, let it be
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double price = null;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> messages = new LinkedList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

}
