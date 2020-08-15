package zm.co.alphabet.springboot.reactor.tutorial.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 10:36 PM
 **/

@WebFluxTest
public class FluxAndMonoTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxApproach1() {
//        VirtualTimeScheduler.getOrSet();
        Flux<Integer> integerFlux = webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux.log())
                .expectSubscription()
                .expectNext(0, 1, 2, 3)
                .verifyComplete();

        /*StepVerifier.withVirtualTime(()->integerFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(1))
                .expectNext(0,1,2,3,4,5,6,7,8,9)
                .verifyComplete();*/


    }

    @Test
    public void fluxApproach2() {
        webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);


    }

    @Test
    public void fluxApproach3() {

        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxApproach4() {

        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3);

        webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedList, response.getResponseBody());
                });

    }

    @Test
    public void fluxStream() {
        Flux<Long> longFlux = webTestClient
                .get()
                .uri("/fluxinfinitystream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFlux.log())
                .expectSubscription()
                .expectNext(0L, 1L, 2L, 3L)
                .thenCancel()
                .verify();
    }

    @Test
    public void monoTest(){
        Integer expectedValue = 1;
        webTestClient
                .get()
                .uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response)->{
                    assertEquals(expectedValue,response.getResponseBody());
                });
    }

}
