package zm.co.alphabet.springboot.reactor.tutorial;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 08/08/2020
 * Time: 6:47 PM
 **/
@Getter
@Setter
public class CustomException extends Throwable {
    private String message;
    public CustomException(Throwable e) {
        this.message = e.getMessage();
    }
}
