package zm.co.alphabet.springboot.reactor.tutorial.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import zm.co.alphabet.springboot.reactor.tutorial.document.Item;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 10/08/2020
 * Time: 1:43 PM
 **/

@DataMongoTest
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveDao itemReactiveDao;

    List<Item> itemList = Arrays.asList(new Item(null, "Iphone X", 1000D),
            new Item(null, "LG TV", 2000D),
            new Item(null, "Apple TV", 2000D),
            new Item(null, "Samsung TV", 1800D),
            new Item(null, "Macbook Pro", 3000D),
            new Item(null, "Macbook", 10000D),
            new Item(null, "Apple Watch", 199.99D),
            new Item(null, "Beats by Dre Headphones", 999D),
            new Item("ABC", "Beats by Dre2 Headphones", 999D)
    );

    @BeforeEach
    public void setUp() {
        itemReactiveDao.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveDao::save)
                .doOnNext((item -> {
                    System.out.println("Inserted Item : " + item);
                }))
                .blockLast(); //ONLY USED IN TEST CASES


    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveDao.findAll().log())
                .expectSubscription()
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void getItemById() {

        StepVerifier.create(itemReactiveDao.findById("ABC"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Beats by Dre2"))
                .verifyComplete();


    }


    @Test
    public void getItemByDescription() {
        StepVerifier.create(itemReactiveDao.findByDescription("Macbook").log("getItemByDescription : "))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getItemByDescriptionContaining() {
        StepVerifier.create(itemReactiveDao.findByDescriptionContaining("Headphones").log("getItemByDescription : "))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Google Home Mini", 30.00);
        StepVerifier.create(itemReactiveDao.save(item))
                .expectSubscription()
                .expectNextMatches(itm -> itm.getId() != null && itm.getDescription().equals("Google Home Mini"))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        double newPrice = 520.99;
       StepVerifier.create(
               itemReactiveDao.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> itemReactiveDao.save(item)))
       .expectSubscription()
       .expectNextMatches(item -> item.getPrice() == newPrice)
       .verifyComplete();


    }

    @Test
    public void deleteItemById(){
        Mono<Void> deletedItem = itemReactiveDao.findById("ABC")
                .map(item -> item.getId())
                .flatMap((id) -> {
                    return itemReactiveDao.deleteById(id);
                });

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveDao.findAll())
                .expectSubscription()
                .expectNextCount(8)
                .verifyComplete();
    }
    @Test
    public void deleteItem(){
        Mono<Void> deletedItem = itemReactiveDao.findById("ABC")
                .flatMap((item) -> {
                    return itemReactiveDao.delete(item);
                });

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveDao.findAll())
                .expectSubscription()
                .expectNextCount(8)
                .verifyComplete();
    }



}
