package zm.co.alphabet.springboot.reactor.tutorial;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 8:19 PM
 **/
public class CodeAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe((element)-> System.out.println("Subscriber 1 value : "+element));

        Thread.sleep(2000);

        stringFlux.subscribe((element)-> System.out.println("Subscriber 2 value : "+element));

        Thread.sleep(10000);

    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe((element)-> System.out.println("Subscriber 1 value : "+element));
        Thread.sleep(2000);

        connectableFlux.subscribe((element)-> System.out.println("Subscriber 2 value : "+element));

        Thread.sleep(10000);
    }
}
