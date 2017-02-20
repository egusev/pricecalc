package ru.erfolk.pricecalc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.erfolk.pricecalc.dtos.PriceCalcResponseDTO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by eugene on 20.02.17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RequestRateTest extends AbstractCalcControllerTest {

    @Test
    public void testUser1Requests() throws InterruptedException {
        waitTillNextSecond();
        testRateRequests("user1", 10);
    }

    @Test
    public void testUser2Requests() throws InterruptedException {
        waitTillNextSecond();
        testRateRequests("user2", 20);
    }

    @Test
    public void testUser3Requests() throws InterruptedException {
        waitTillNextSecond();
        testRateRequests("user3", 50);
    }

    @Test
    public void testDiffUsers() throws InterruptedException {
        waitTillNextSecond();

        ExecutorService taskExecutor = Executors.newFixedThreadPool(5);

        taskExecutor.execute(() -> testRateRequests("user1", 10));
        taskExecutor.execute(() -> testRateRequests("user2", 20));
        taskExecutor.execute(() -> testRateRequests("user3", 50));
        taskExecutor.execute(() -> testRateRequests("user4", 100));

        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
    }

    public void testRateRequests(String user, int expectedSuccessRequests) {
        int[] success = new int[]{0};
        int size = 100;

        ExecutorService taskExecutor = Executors.newFixedThreadPool(size);

        for (int i = 0; i < size; i++) {
            taskExecutor.execute(() -> {
                if (fastCall(user)) {
                    synchronized (success) {
                        success[0]++;
                    }
                }
            });
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(expectedSuccessRequests, success[0]);
    }

    private boolean fastCall(String user) {
        try {
            PriceCalcResponseDTO responseDTO = (PriceCalcResponseDTO) mvc.perform(
                    calcRequest(user)
                            .content("{\"isin\":\"123456789012\",\"value\":\"10\",\"volatility\":\"0\"}")
            )
                    .andExpect(status().isOk())
                    .andReturn().getAsyncResult();
            return responseDTO.isSuccess();
        } catch (Throwable e) {
            return false;
        }
    }

    private void waitTillNextSecond() throws InterruptedException {
        long millisWithinSecond = System.currentTimeMillis() % 1000;
        Thread.sleep(1000 - millisWithinSecond);
        log.debug("start at {}", System.currentTimeMillis());
    }
}
