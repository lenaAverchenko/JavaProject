package telran.functionality.com.dto;
/**
 * Class ClientCreateDto - provided by user object, given to convert into Client entity
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientCreateDto {

    @Schema(description = "Login", defaultValue = "Client")
    private String login;

    @Schema(description = "Password", defaultValue = "Client456")
    private String password;

    @Schema(description = "Product identifier", defaultValue = "1")
    private long managerId;

    @Schema(description = "Personal code of client", defaultValue = "PH11111")
    private String taxCode;

    @Schema(description = "Name", defaultValue = "Tim")
    private String firstName;

    @Schema(description = "Surname", defaultValue = "Timson")
    private String lastName;

    @Schema(description = "e-mail address", defaultValue = "tim@gmail.com")
    private String email;

    @Schema(description = "Address", defaultValue = "Main Street 80")
    private String address;

    @Schema(description = "Phone number", defaultValue = "123456789")
    private String phone;
}