package telran.functionality.com.dto;
/**
 * Class ClientDto - to create object from Client entity, which will be shown to user
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

    private UUID id;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String firstName;
    private String lastName;
    private ManagerDto manager;
    private List<AccountDto> accounts = new ArrayList<>();
}
