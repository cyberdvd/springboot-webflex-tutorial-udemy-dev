package zm.co.alphabet.springboot.reactor.tutorial.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12/08/2020
 * Time: 1:14 PM
 **/
@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemCapped {
    @Id
    private String id;
    private String description;
    private Double price;
}
