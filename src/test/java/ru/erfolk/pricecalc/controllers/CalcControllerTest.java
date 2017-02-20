package ru.erfolk.pricecalc.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by eugene on 20.02.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CalcControllerTest extends AbstractCalcControllerTest {

    @Test
    public void testValidRequests() throws Exception {
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\",\"volatility\":\"10000\"}")
        ).andExpect(status().isOk());

        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890Ab\",\"value\":\"0.0001\",\"volatility\":\"0.00\"}")
        ).andExpect(status().isOk());

        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"abcdefghijkl\",\"value\":\"100\",\"volatility\":\"0010000.00\"}")
        ).andExpect(status().isOk());
    }


    @Test
    public void testInvalidRequests() throws Exception {
        // volatility is absent
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\"}")
        ).andExpect(status().isBadRequest());

        // volatility is empty
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\",\"volatility\":\"\"}")
        ).andExpect(status().isBadRequest());

        // volatility is negative
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\",\"volatility\":\"-1\"}")
        ).andExpect(status().isBadRequest());

        // volatility is too big
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\",\"volatility\":\"10000.0001\"}")
        ).andExpect(status().isBadRequest());

        // value is absent
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"volatility\":\"0\"}")
        ).andExpect(status().isBadRequest());

        // value is empty
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"\",\"volatility\":\"0\"}")
        ).andExpect(status().isBadRequest());

        // value is negative
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"-1\",\"volatility\":\"0\"}")
        ).andExpect(status().isBadRequest());

        // value is too big
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"100.0001\",\"volatility\":\"0\"}")
        ).andExpect(status().isBadRequest());


        // isin is absent
        mvc.perform(
                calcRequest()
                        .content("{\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isBadRequest());

        // isin contains an incorrect symbols
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890  \",\"value\":\"0\",\"volatility\":\"10000\"}")
        ).andExpect(status().isBadRequest());
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890/2\",\"value\":\"0\",\"volatility\":\"10000\"}")
        ).andExpect(status().isBadRequest());
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123.45678901\",\"value\":\"0\",\"volatility\":\"10000\"}")
        ).andExpect(status().isBadRequest());

        // isin is too short
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890\",\"value\":\"0\",\"volatility\":\"10000\"}")
        ).andExpect(status().isBadRequest());

        // isin is too big
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890123\",\"value\":\"0\",\"volatility\":\"10000\"}")
        ).andExpect(status().isBadRequest());
    }
}