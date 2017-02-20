package ru.erfolk.pricecalc.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.erfolk.pricecalc.dtos.PriceCalcResponseDTO;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by eugene on 20.02.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-sleeping")
public class CalcControllerTimeoutTest extends AbstractCalcControllerTest {
    @Test
    public void testLongValidRequests() throws Exception {
        PriceCalcResponseDTO responseDTO;

        responseDTO = (PriceCalcResponseDTO) mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"10\",\"volatility\":\"2990\"}")
        )
                .andDo(print())
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
                        .content("{\"isin\":\"123456789012\",\"value\":\"10\",\"volatility\":\"3010\"}")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assert.assertNull(responseDTO.getPrice());
        Assert.assertFalse(responseDTO.isSuccess());
        Assert.assertEquals(1, responseDTO.getMessages().size());
        Assert.assertEquals("Calculation time is out", responseDTO.getMessages().get(0));
        Assert.assertTrue((System.currentTimeMillis() - responseDTO.getDate().getTime()) < TIME_DIFF);
    }

}