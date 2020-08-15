package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 7:35 PM
 **/
public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(0, 10);

        StepVerifier.create(integerFlux.log())
                .expectSubscription()
                .thenRequest(1)
                .expectNext(0)
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressure() {
        Flux<Integer> integerFlux = Flux.range(0, 10);

        integerFlux.subscribe((element) -> System.out.println("Element is : " + element)
                , (e) -> System.err.println("Exception occurred : " + e.getMessage())
                , () -> System.out.println("Done")
                , (subscription) -> subscription.request(4));
    }

    @Test
    public void backPressureWithCancel() {
        Flux<Integer> integerFlux = Flux.range(0, 10);

        integerFlux.subscribe((element) -> System.out.println("Element is : " + element)
                , (e) -> System.err.println("Exception occurred : " + e.getMessage())
                , () -> System.out.println("Done")
                , (subscription) -> subscription.cancel());
    }

    @Test
    public void customizedBackPressure() {
        Flux<Integer> integerFlux = Flux.range(0, 10)
                .log();

        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);

                request(1);
                System.out.println("Received value : "+value);
                if(value==5){
                    cancel();
                }

            }
        });
    }
}
