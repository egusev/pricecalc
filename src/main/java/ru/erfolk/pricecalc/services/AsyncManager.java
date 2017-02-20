package ru.erfolk.pricecalc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import ru.erfolk.pricecalc.dtos.PriceCalcResponseDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by eugene on 20.02.17.
 */
@Slf4j
@Service
public class AsyncManager {
    @Autowired
    private ExecutorService executor;

    @Value("${calculation.timeout}")
    private Integer TIMEOUT;

    public DeferredResult<PriceCalcResponseDTO> asyncCalculation(Calculator calculator) {
        final DeferredResult<PriceCalcResponseDTO> result = new DeferredResult<>();
        final Future<BigDecimal> future = executor.submit(calculator::calculate);
        new Thread(() -> completeResponse(result, future)).start();

        return result;
    }

    private void completeResponse(DeferredResult<PriceCalcResponseDTO> result, Future<BigDecimal> function) {
        PriceCalcResponseDTO dto = new PriceCalcResponseDTO();
        try {
            dto.setPrice(function.get(TIMEOUT, TimeUnit.MILLISECONDS));
            dto.setSuccess(true);
        } catch( TimeoutException e) {
            log.warn(e.getMessage(), e);
            dto.setSuccess(false);
            dto.addMessage("Calculation time is out");
        } catch (ExecutionException | InterruptedException e) {
            log.warn(e.getMessage(), e);
            dto.setSuccess(false);
            dto.addMessage(e.getMessage());
        }
        finally {
            dto.setDate(new Date());
            result.setResult(dto);
        }
    }

}
