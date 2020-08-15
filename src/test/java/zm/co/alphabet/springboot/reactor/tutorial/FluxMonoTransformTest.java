package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 07/08/2020
 * Time: 4:55 PM
 **/
public class FluxMonoTransformTest {

    private List<String> names = Arrays.asList("David", "Mulenga", "Chilekwa", "Bwalya", "Aubrey", "Mutonkolo", "Mwiche", "Michael");

    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("DAVID", "MULENGA", "CHILEKWA", "BWALYA", "AUBREY", "MUTONKOLO", "MWICHE", "MICHAEL")
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLength() {
        Flux<Integer> integerFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();

        StepVerifier.create(integerFlux)
                .expectNext(5, 7, 8, 6, 6, 9, 6, 7)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLengthRepeat() {
        Flux<Integer> integerFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .repeat(1)
                .log();

        StepVerifier.create(integerFlux)
                .expectNext(5, 7, 8, 6, 6, 9, 6, 7, 5, 7, 8, 6, 6, 9, 6, 7)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLengthRepeatFilter() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 6)
                .map(s -> s.toUpperCase())
                .repeat(1)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("MULENGA", "CHILEKWA", "MUTONKOLO", "MICHAEL", "MULENGA", "CHILEKWA", "MUTONKOLO", "MICHAEL")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s)); // A -> List[A,newValue], B -> List[B,newValue]
                }) //db or external service call that returns a flux -> s -> Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    } @Test
    public void transformUsingFlatMapUsingParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))//Flux<String>
                .window(2)//Flux<Flux<String>> -> (A,B), (C,D),(E,F)
                .flatMap((s) ->
                    s.map(this::convertToList).subscribeOn(parallel()) //Flux<List<String>>
                ).flatMap(s -> Flux.fromIterable(s)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
 @Test
    public void transformUsingFlatMapUsingParallelOrdered() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))//Flux<String>
                .window(2)//Flux<Flux<String>> -> (A,B), (C,D),(E,F)
                /*.concatMap((s) ->
                    s.map(this::convertToList).subscribeOn(parallel()) //Flux<List<String>>

                )*/
                .flatMapSequential((s) ->
                    s.map(this::convertToList).subscribeOn(parallel()) //Flux<List<String>>

                ).flatMap(s -> Flux.fromIterable(s)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.asList(s, "newValue");

    }

}
