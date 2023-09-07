package telran.functionality.com.dto;
/**
 * Class AccountDto - to create object from Account entity, which will be shown to user
 *
 * @author Olena Averchenko
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.functionality.com.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
    private ClientDto clientDto;
}