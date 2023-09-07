package telran.functionality.com.dto;
/**
 * Class ClientCreateDto - provided by user object, given to convert into Client entity
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
public class ClientCreateDto {

    private String login;
    private String password;
    private long managerId;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}