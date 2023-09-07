package telran.functionality.com.dto;
/**
 * Class ManagerCreateDto - provided by user object, given to convert into Manager entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerCreateDto {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
}