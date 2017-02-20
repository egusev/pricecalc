package ru.erfolk.pricecalc.controllers;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.erfolk.pricecalc.Endpoints;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Created by eugene on 20.02.17.
 */
abstract class AbstractCalcControllerTest {

    // time in ms to get data from the controller
    public static final long TIME_DIFF = 50;
    
    protected MockMvc mvc;

    @Autowired
    private CalcController calcController;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(calcController).build();
    }

    protected final MockHttpServletRequestBuilder calcRequest() {
        return put(Endpoints.PRICE_CALC)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);
    }
}
