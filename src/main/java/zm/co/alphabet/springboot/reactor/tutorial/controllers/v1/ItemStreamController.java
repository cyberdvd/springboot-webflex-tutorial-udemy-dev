package zm.co.alphabet.springboot.reactor.tutorial.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import zm.co.alphabet.springboot.reactor.tutorial.constants.ItemConstants;
import zm.co.alphabet.springboot.reactor.tutorial.document.ItemCapped;
import zm.co.alphabet.springboot.reactor.tutorial.repository.ItemReactiveCappedRepository;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12/08/2020
 * Time: 2:28 PM
 **/
@RestController
@RequiredArgsConstructor
public class ItemStreamController {

    private final ItemReactiveCappedRepository itemReactiveCappedRepository;

    @GetMapping(value = ItemConstants.ITEM_STREAM_END_POINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemsStream(){
        return itemReactiveCappedRepository.findItemsBy();
    }

}
