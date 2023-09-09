package telran.functionality.com.dto;
/**
 * Class ClientDto - to create object from Client entity, which will be shown to user
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.functionality.com.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

    @Schema(description = "Client identifier",defaultValue = "246bc91b-613d-4d0b-bb1d-000037199fcc")
    private UUID id;

    @Schema(description = "Status of client",defaultValue = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Name", defaultValue = "Tim")
    private String firstName;

    @Schema(description = "Surname", defaultValue = "Timson")
    private String lastName;

    @Schema(description = "Manager of client", defaultValue = "{\"uniqueId\": \"507d8422-b593-4a41-95bf-cc5c5bbfa851\",\n" +
            "    \"status\": \"ACTIVE\",\n" +
            "    \"firstName\": \"Postman Third\",\n" +
            "    \"lastName\": \"Postman Third LastName\",\n" +
            "    \"manager\": {\n" +
            "        \"id\": 7,\n" +
            "        \"firstName\": \"managerSecond\",\n" +
            "        \"lastName\": \"ThroughPostman\",\n" +
            "        \"status\": \"ACTIVE\"\n" +
            "    },\n" +
            "    \"accounts\": []\n}")
    private ManagerDto manager;

    @Schema(description = "Accounts",
            defaultValue = "[\n" +
                    "        {\n" +
                    "            \"id\": \"c2365255-4125-428a-84d3-5affff120b02\",\n" +
                    "            \"name\": \"Personal Account\",\n" +
                    "            \"status\": \"ACTIVE\",\n" +
                    "            \"clientDto\": {\n" +
                    "                \"uniqueId\": \"a63ffd5f-af57-413b-aab9-443a0784c11c\",\n" +
                    "                \"status\": \"ACTIVE\",\n" +
                    "                \"firstName\": \"Michail\",\n" +
                    "                \"lastName\": \"Michailov\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]\n")
    private List<AccountDto> accounts = new ArrayList<>();

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", status=" + status +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
