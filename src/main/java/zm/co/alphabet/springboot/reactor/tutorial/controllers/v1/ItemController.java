package zm.co.alphabet.springboot.reactor.tutorial.controllers.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zm.co.alphabet.springboot.reactor.tutorial.constants.ItemConstants;
import zm.co.alphabet.springboot.reactor.tutorial.document.Item;
import zm.co.alphabet.springboot.reactor.tutorial.repository.ItemReactiveDao;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 10/08/2020
 * Time: 4:54 PM
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemReactiveDao itemReactiveDao;



    @GetMapping(ItemConstants.ITEM_END_POINT_V1_ALL)
    public Flux<Item> getAllItems() {
        return itemReactiveDao.findAll();
    }

    @GetMapping(ItemConstants.ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable String id) {
        return itemReactiveDao.findById(id)
                .map((item -> new ResponseEntity<>(item, HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ItemConstants.ITEM_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveDao.save(item);
    }

    @DeleteMapping(ItemConstants.ITEM_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable("id") String id) {
        return itemReactiveDao.deleteById(id);
    }

    @PutMapping(ItemConstants.ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable("id") String id, @RequestBody Item item) {
        return itemReactiveDao.findById(id)
                .flatMap(persistedItem -> {
                    persistedItem.setPrice(item.getPrice());
                    persistedItem.setDescription(item.getDescription());
                    return itemReactiveDao.save(persistedItem);
                }).map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(ItemConstants.ITEM_END_POINT_V1_ALL+"/runtimeException")
    public Flux<Item> runtimeException()
    {
        return itemReactiveDao.findAll()
                .concatWith(Mono.error(new RuntimeException("Runtime exception occurred")));
    }
}
