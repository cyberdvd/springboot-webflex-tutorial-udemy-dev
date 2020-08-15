package zm.co.alphabet.springboot.reactor.tutorial.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import zm.co.alphabet.springboot.reactor.tutorial.document.ItemCapped;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12/08/2020
 * Time: 2:17 PM
 **/
public interface ItemReactiveCappedRepository extends ReactiveMongoRepository<ItemCapped,String> {
    @Tailable
    Flux<ItemCapped> findItemsBy();
}
