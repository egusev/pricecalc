package ru.erfolk.pricecalc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.erfolk.pricecalc.Endpoints;
import ru.erfolk.pricecalc.dtos.PriceCalcResponseDTO;

/**
 * Created by eugene on 20.02.17.
 */
@RestController
public class CalcController {

    @RequestMapping(value = Endpoints.PRICE_CALC, method = RequestMethod.GET)
    public PriceCalcResponseDTO calculatePrice() {
        return new PriceCalcResponseDTO();
    }

}
