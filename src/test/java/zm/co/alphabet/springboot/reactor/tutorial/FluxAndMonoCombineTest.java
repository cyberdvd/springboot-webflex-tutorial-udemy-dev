package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 5:47 PM
 **/
public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> stringFlux1 = Flux.just("A", "B", "C");
        Flux<String> stringFlux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.merge(stringFlux1, stringFlux2)
                .log();

        StepVerifier.create(mergedFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingMergeWithDelay() {
        Flux<String> stringFlux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(stringFlux1, stringFlux2);

        StepVerifier.create(mergedFlux.log())
                .expectNextCount(6)
//                .expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat() {
        Flux<String> stringFlux1 = Flux.just("A", "B", "C");
        Flux<String> stringFlux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.concat(stringFlux1, stringFlux2)
                .log();

        StepVerifier.create(mergedFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingCombineWithDelay() {
        Flux<String> stringFlux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(stringFlux1, stringFlux2);

        StepVerifier.create(mergedFlux.log())
                .expectNextCount(6)
//                .expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }

    @Test
    public void combineUsingCombineWithDelayVirtualTime() {
        VirtualTimeScheduler.getOrSet();

        Flux<String> stringFlux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(stringFlux1, stringFlux2);

        StepVerifier.withVirtualTime(()->mergedFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNextCount(6)
                .verifyComplete();


    }

    @Test
    public void combineUsingZip() {
        Flux<String> stringFlux1 = Flux.just("A", "B", "C");
        Flux<String> stringFlux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.zip(stringFlux1, stringFlux2, (t1, t2) -> {
            return t1.concat(t2);
        });

        StepVerifier.create(mergedFlux.log())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }

    @Test
    public void combineUsingZipWithDelay() {
        Flux<String> stringFlux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.zip(stringFlux1, stringFlux2, (t1, t2) -> t1.concat(t2));

        StepVerifier.create(mergedFlux.log())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }
}
