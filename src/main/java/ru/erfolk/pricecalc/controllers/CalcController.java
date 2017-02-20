package ru.erfolk.pricecalc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import ru.erfolk.pricecalc.Endpoints;
import ru.erfolk.pricecalc.actors.Actor;
import ru.erfolk.pricecalc.dtos.PriceCalcRequestDTO;
import ru.erfolk.pricecalc.dtos.PriceCalcResponseDTO;
import ru.erfolk.pricecalc.services.AsyncManager;
import ru.erfolk.pricecalc.services.Calculator;
import ru.erfolk.pricecalc.services.CalculatorService;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by eugene on 20.02.17.
 */
@Slf4j
@RestController
@DependsOn
public class CalcController {
    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private AsyncManager asyncManager;

    @RequestMapping(value = Endpoints.PRICE_CALC, method = RequestMethod.PUT)
    public DeferredResult<PriceCalcResponseDTO> calculatePrice(@AuthenticationPrincipal Actor actor,
                                                               @Valid @RequestBody PriceCalcRequestDTO dto) {
        if (actor.canProcess()) {
            Calculator calculator = calculatorService.calculate(dto.getIsin(), dto.getValue(), dto.getVolatility());
            return asyncManager.asyncCalculation(calculator);

        } else {
//            log.warn("Request count {} is exceeded {}", actor.getUser().getRequestPerSecond(), actor.getRequests());
            DeferredResult<PriceCalcResponseDTO> result = new DeferredResult<>();
            PriceCalcResponseDTO responseDTO = new PriceCalcResponseDTO();
            responseDTO.setDate(new Date());
            responseDTO.setSuccess(false);
            responseDTO.addMessage("Request count is exceeded");
            result.setResult(responseDTO);
            return result;
        }
    }
}
