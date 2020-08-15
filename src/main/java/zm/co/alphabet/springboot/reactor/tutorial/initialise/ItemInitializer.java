package zm.co.alphabet.springboot.reactor.tutorial.initialise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import zm.co.alphabet.springboot.reactor.tutorial.document.Item;
import zm.co.alphabet.springboot.reactor.tutorial.document.ItemCapped;
import zm.co.alphabet.springboot.reactor.tutorial.repository.ItemReactiveCappedRepository;
import zm.co.alphabet.springboot.reactor.tutorial.repository.ItemReactiveDao;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 10/08/2020
 * Time: 5:21 PM
 **/
@Component
@RequiredArgsConstructor
@Profile("!test")
@Slf4j
public class ItemInitializer implements CommandLineRunner {

    private final ItemReactiveDao itemReactiveDao;
    private final ItemReactiveCappedRepository itemReactiveCappedRepository;

    private final ReactiveMongoOperations mongoOperations;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();

        createCappedCollection();

        dataSetupCappedCollection();
    }

    private void createCappedCollection() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());

    }

    public List<Item> data(){
        return Arrays.asList(new Item(null, "Iphone X", 1000D),
                new Item(null, "LG TV", 1999.99),
                new Item(null, "Apple TV", 1999.99),
                new Item(null, "Samsung TV", 1949.99),
                new Item(null, "Macbook Pro", 1500.0),
                new Item(null, "Macbook", 2999.99),
                new Item(null, "Apple Watch", 199.99D),
                new Item(null, "Bose Headphones", 199.99),
                new Item("ABC", "Beats by Dre2 Headphones", 199.99)
        );

    }

    private void initialDataSetup() {

        itemReactiveDao.deleteAll().
        thenMany(Flux.fromIterable(data()))
        .flatMap(itemReactiveDao::save)
        .thenMany(itemReactiveDao.findAll())
        .subscribe(item -> System.out.println("Inserted from CommandLine runner: "+item));

    }

    private void dataSetupCappedCollection(){
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "Random Item " + i, (100.00 + i)));

        itemReactiveCappedRepository.insert(itemCappedFlux)
                .subscribe(itemCapped -> log.info("Inserted Item is : "+itemCapped));

    }
}
