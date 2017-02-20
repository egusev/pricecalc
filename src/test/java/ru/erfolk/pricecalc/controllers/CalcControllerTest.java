package ru.erfolk.pricecalc.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.erfolk.pricecalc.Endpoints;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by eugene on 20.02.17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CalcController.class, secure = false)
public class CalcControllerTest {
    @Autowired
    private MockMvc mvc;
/*

    private JacksonTester<PriceCalcRequestDTO> json_request;

    private JacksonTester<PriceCalcResponseDTO> json_response;
*/

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testValidRequests() throws Exception {
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isOk());

        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890Ab\",\"value\":\"0.0001\",\"volatility\":\"0.00\"}")
        ).andExpect(status().isOk());

        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"abcdefghijkl\",\"value\":\"100\",\"volatility\":\"00100.00\"}")
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
                        .content("{\"isin\":\"123456789012\",\"value\":\"0\",\"volatility\":\"100.0001\"}")
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
                        .content("{\"isin\":\"1234567890  \",\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isBadRequest());
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890/2\",\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isBadRequest());
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"123.45678901\",\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isBadRequest());

        // isin is too short
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890\",\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isBadRequest());

        // isin is too big
        mvc.perform(
                calcRequest()
                        .content("{\"isin\":\"1234567890123\",\"value\":\"0\",\"volatility\":\"100\"}")
        ).andExpect(status().isBadRequest());
    }

    private static MockHttpServletRequestBuilder calcRequest() {
        return put(Endpoints.PRICE_CALC)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);
    }
}