package zm.co.alphabet.springboot.reactor.tutorial.controller.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import zm.co.alphabet.springboot.reactor.tutorial.constants.ItemConstants;
import zm.co.alphabet.springboot.reactor.tutorial.document.Item;
import zm.co.alphabet.springboot.reactor.tutorial.repository.ItemReactiveDao;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 10/08/2020
 * Time: 5:43 PM
 **/
@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveDao itemReactiveDao;

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Iphone X", 1000D),
                new Item(null, "LG TV", 2000D),
                new Item(null, "Apple TV", 2000D),
                new Item(null, "Samsung TV", 1800D),
                new Item(null, "Macbook Pro", 3000D),
                new Item(null, "Macbook", 10000D),
                new Item(null, "Apple Watch", 199.99D),
                new Item(null, "Beats by Dre Headphones", 999D),
                new Item("ABC", "Beats by Dre2 Headphones", 999D)
        );

    }


    @BeforeEach
    public void setUp() {
        itemReactiveDao.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveDao::save)
                .doOnNext(item -> System.out.println("Inserted Item : " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1_ALL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(9);
    }

    @Test
    public void getAllItemsApproach2() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1_ALL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(9)
                .consumeWith((response) -> {
                    List<Item> items = response.getResponseBody();
                    items.forEach((item -> {
                        assertTrue(item.getId() != null);
                    }));
                });
    }

    @Test
    public void getAllItemsApproach3() {
        Flux<Item> responseBody = webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1_ALL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    public void getItem() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 99.0);

    }


    @Test
    public void createItem() {
        Item item = new Item(null, "Iphone 11", 999.9);

        webTestClient.post()
                .uri(ItemConstants.ITEM_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone 11")
                .jsonPath("$.price").isEqualTo(999.9);

    }


    @Test
    public void deleteItem() {
        webTestClient.delete().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem() {
        double newPrice = 199.99;
        Item item = new Item(null, "Beats by Dre2 Headphones", newPrice);

        webTestClient.put().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", newPrice);
    }

    @Test
    public void runtimeException(){
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1_ALL+"/runtimeException")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Runtime exception occurred");
    }

}
