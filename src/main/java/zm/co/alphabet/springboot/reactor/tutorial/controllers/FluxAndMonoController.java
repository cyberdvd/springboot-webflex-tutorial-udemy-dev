package zm.co.alphabet.springboot.reactor.tutorial.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 10:21 PM
 **/
@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> getFlux(){
        return Flux.range(0,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }
    @GetMapping(value = "/fluxstream",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getFluxStream(){
        return Flux.range(0,20)
                .delayElements(Duration.ofSeconds(2))
                .log();
    }
    @GetMapping(value = "/fluxinfinitystream",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> getFluxInfinityStream(){
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/mono")
    public Mono<Integer> getMono()
    {
        return Mono.just(1)
                .log();
    }

}
