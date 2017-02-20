package ru.erfolk.pricecalc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.erfolk.pricecalc.dtos.PriceCalcResponseDTO;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by eugene on 20.02.17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-sleeping")
public class CalcControllerTimeoutTest extends AbstractCalcControllerTest {

    @Test
    public void testLongValidRequests() throws Exception {
        PriceCalcResponseDTO responseDTO;

        responseDTO = (PriceCalcResponseDTO) mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"10\",\"volatility\":\"990\"}")
        )
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assert.assertEquals(BigDecimal.TEN, responseDTO.getPrice());
        Assert.assertTrue(responseDTO.isSuccess());
        Assert.assertTrue(responseDTO.getMessages().isEmpty());
        Assert.assertTrue((System.currentTimeMillis() - responseDTO.getDate().getTime()) < TIME_DIFF);
    }

    @Test
    public void testLongTimeOutedRequests() throws Exception {
        PriceCalcResponseDTO responseDTO;

        responseDTO = (PriceCalcResponseDTO) mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"10\",\"volatility\":\"1100\"}")
        )
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assert.assertNull(responseDTO.getPrice());
        Assert.assertFalse(responseDTO.isSuccess());
        Assert.assertEquals(1, responseDTO.getMessages().size());
        Assert.assertEquals("Calculation time is out", responseDTO.getMessages().get(0));
        Assert.assertTrue((System.currentTimeMillis() - responseDTO.getDate().getTime()) < TIME_DIFF);
    }

    @Test
    public void testValidManyCallsAtOnceRequests() throws Exception {
        int[] errors = new int[]{0};
        int size = 100;

        ExecutorService taskExecutor = Executors.newFixedThreadPool(size);

        for (int i = 0; i < size; i++) {
            taskExecutor.execute(() -> {
                if (!fastCall(400)) {
                    synchronized (errors) {
                        errors[0]++;
                    }
                }
            });
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(0, errors[0]);
    }


    @Test
    public void testInvalidManyCallsAtOnceRequests() throws Exception {
        int[] errors = new int[]{0};
        int size = 100;

        ExecutorService taskExecutor = Executors.newFixedThreadPool(size);

        for (int i = 0; i < size; i++) {
            taskExecutor.execute(() -> {
                if (!fastCall(2000)) {
                    synchronized (errors) {
                        errors[0]++;
                    }
                }
            });
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(100, errors[0]);
    }

    private boolean fastCall(long pause) {
        try {
            PriceCalcResponseDTO responseDTO = (PriceCalcResponseDTO) mvc.perform(
                    calcRequest()
                            .content("{\"isin\":\"123456789012\",\"value\":\"10\",\"volatility\":\"" + pause + "\"}")
            )
                    .andExpect(status().isOk())
                    .andExpect(request().asyncStarted())
                    .andReturn().getAsyncResult();
            return responseDTO.isSuccess();
        } catch (Throwable e) {
            return false;
        }
    }

}