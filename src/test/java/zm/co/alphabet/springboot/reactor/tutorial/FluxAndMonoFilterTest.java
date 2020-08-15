package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 07/08/2020
 * Time: 4:47 PM
 **/
public class FluxAndMonoFilterTest {

    private List<String> names = Arrays.asList("David","Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael");


    @Test
    public void filterTest(){
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("M"))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Mulenga","Mutonkolo","Mwiche","Michael")
                .verifyComplete();
    }

    @Test
    public void filterTestLength(){
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.length()>5)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Mulenga","Chilekwa","Bwalya","Aubrey","Mutonkolo","Mwiche","Michael")
                .verifyComplete();
    }
}
