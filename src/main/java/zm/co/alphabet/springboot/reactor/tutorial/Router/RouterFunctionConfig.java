package zm.co.alphabet.springboot.reactor.tutorial.Router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import zm.co.alphabet.springboot.reactor.tutorial.handler.SampleHandlerFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 09/08/2020
 * Time: 12:17 AM
 **/
@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> route(SampleHandlerFunction handlerFunction){
        return RouterFunctions.route(GET("/function/flux").and(accept(MediaType.APPLICATION_JSON)),handlerFunction::flux)
                .andRoute(GET("/function/mono").and(accept(MediaType.APPLICATION_JSON)),handlerFunction::mono);
    }

}
