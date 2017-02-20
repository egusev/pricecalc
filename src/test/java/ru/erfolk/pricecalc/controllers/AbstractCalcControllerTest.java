package ru.erfolk.pricecalc.controllers;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.erfolk.pricecalc.Endpoints;
import ru.erfolk.pricecalc.actors.Actor;
import ru.erfolk.pricecalc.services.UserService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

/**
 * Created by eugene on 20.02.17.
 */
abstract class AbstractCalcControllerTest {

    // time in ms to get data from the controller
    public static final long TIME_DIFF = 50;

    @Autowired
   	private WebApplicationContext context;

    protected MockMvc mvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    protected final MockHttpServletRequestBuilder calcRequest() {
        return calcRequest("user4");
    }

    protected final MockHttpServletRequestBuilder calcRequest(String user) {
        Actor actor = userService.loadUserByUsername(user);

        return put(Endpoints.PRICE_CALC)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(actor));
    }
}
