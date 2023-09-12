package telran.functionality.com.dto;
/**
 * Class AccountDto - to create object from Account entity, which will be shown to user
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
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Schema(description = "Account identifier", defaultValue = "246bc91b-613d-4d0b-bb1d-ad9837199fcc")
    private UUID id;

    @Schema(description = "Name of account", defaultValue = "My personal account")
    private String name;

    @Schema(description = "Status of account", defaultValue = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Client-owner of the account",
            defaultValue = "{\n" +
                    "            \"uniqueId\": \"21a6278f-31d4-43df-8576-46875b72d5f9\",\n" +
                    "            \"status\": \"ACTIVE\",\n" +
                    "            \"firstName\": \"Bank\",\n" +
                    "            \"lastName\": \"Bank\"\n" +
                    "        }\n")
    private ClientDto clientDto;

    @Override
    public String toString() {
        return "AccountDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}