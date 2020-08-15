package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 7:15 PM
 **/
public class FluxWithMonoTimeTest {

    @Test
    public void infinitySequence() throws InterruptedException {
        Flux<Long> infinityFlux = Flux.interval(Duration.ofMillis(100))
                .log();

        infinityFlux.subscribe((element)-> System.out.println("Generated Value : "+element));

        Thread.sleep(3000);

    } @Test
    public void infinitySequenceTest() throws InterruptedException {
        Flux<Long> infinityFlux = Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();

        StepVerifier.create(infinityFlux)
                .expectSubscription()
                .expectNext(0L,1L,2L)
                .verifyComplete();
    }@Test
    public void infinitySequenceMapTest() throws InterruptedException {
        Flux<Integer> infinityFlux = Flux.interval(Duration.ofMillis(100))
                .map(l-> Integer.valueOf(l.intValue()))
                .take(3)
                .log();

        StepVerifier.create(infinityFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }@Test
    public void infinitySequenceMapWithDelayTest() throws InterruptedException {
        Flux<Integer> infinityFlux = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(l-> Integer.valueOf(l.intValue()))
                .take(3)
                .log();

        StepVerifier.create(infinityFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }
}
