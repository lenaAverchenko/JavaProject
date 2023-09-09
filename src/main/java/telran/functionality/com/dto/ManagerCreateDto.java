package telran.functionality.com.dto;
/**
 * Class ManagerCreateDto - provided by user object, given to convert into Manager entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerCreateDto {

    @Schema(description = "Login", defaultValue = "Admin")
    private String login;

    @Schema(description = "Password", defaultValue = "Admin456")
    private String password;

    @Schema(description = "Name", defaultValue = "Filip")
    private String firstName;

    @Schema(description = "Surname", defaultValue = "Mashkovski")
    private String lastName;
}