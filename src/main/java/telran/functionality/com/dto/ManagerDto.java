package telran.functionality.com.dto;
/**
 * Class ManagerDto - to create object from Manager entity, which will be shown to user
 *
 * @author Olena Averchenko
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.functionality.com.enums.Status;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDto {

    @Schema(description = "Manager identifier", defaultValue = "1")
    private long id;

    @Schema(description = "Name", defaultValue = "Filip")
    private String firstName;

    @Schema(description = "Surname", defaultValue = "Mashkovski")
    private String lastName;

    @Schema(description = "Status of the manager", defaultValue = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Clients",
            defaultValue = "[\n" +
                    "        {\n" +
                    "            \"uniqueId\": \"7e1a52e2-27b7-484d-9951-58c88d705cee\",\n" +
                    "            \"status\": \"ACTIVE\",\n" +
                    "            \"firstName\": \"Nikolaj\",\n" +
                    "            \"lastName\": \"Nikolaev\"\n" +
                    "        }\n" +
                    "    ]\n")
    private List<ClientDto> clients = new ArrayList<>();

    @Schema(description = "products",
            defaultValue = "[\n" +
                    "        {\n" +
                    "            \"id\": 8,\n" +
                    "            \"name\": \"mortgage\",\n" +
                    "            \"status\": \"ACTIVE\",\n" +
                    "            \"limitValue\": 300000\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 9,\n" +
                    "            \"name\": \"classic\",\n" +
                    "            \"status\": \"ACTIVE\",\n" +
                    "            \"limitValue\": 5000\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}\n")
    private List<ProductDto> products = new ArrayList<>();

    @Override
    public String toString() {
        return "ManagerDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                '}';
    }
}