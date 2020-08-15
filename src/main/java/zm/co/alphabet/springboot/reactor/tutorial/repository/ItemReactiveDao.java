package zm.co.alphabet.springboot.reactor.tutorial.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import zm.co.alphabet.springboot.reactor.tutorial.document.Item;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 10/08/2020
 * Time: 1:40 PM
 **/
public interface ItemReactiveDao extends ReactiveMongoRepository<Item,String> {

    Flux<Item> findByDescription(String description);
    Flux<Item> findByDescriptionContaining(String description);

}
