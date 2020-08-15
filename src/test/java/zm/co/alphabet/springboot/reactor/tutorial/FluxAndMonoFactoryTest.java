package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 07/08/2020
 * Time: 4:24 PM
 **/
public class FluxAndMonoFactoryTest {

    private List<String> names = Arrays.asList("David","Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael");

    @Test
    public void fluxUsingIterable(){
        Flux<String> stringFlux = Flux.fromIterable(names)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("David","Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray(){
        String[] names = new String[]{"David","Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael"};

        Flux<String> stringFlux = Flux.fromArray(names)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("David","Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream(){
        Flux<String> stringFlux = Flux.fromStream(names.stream())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("David","Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael")
                .verifyComplete();

    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> stringMono = Mono.justOrEmpty(null);

        StepVerifier.create(stringMono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Supplier<String> stringSupplier = ()->"David";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMono.log())
                .expectNext("David")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){
     Flux<Integer> integerFlux =   Flux.range(0,6)
             .log();
     StepVerifier.create(integerFlux)
             .expectNext(0,1,2,3,4,5)
             .verifyComplete();
    }
}
