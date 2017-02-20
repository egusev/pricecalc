package ru.erfolk.pricecalc.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal price = null;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> messages = new LinkedList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

}
