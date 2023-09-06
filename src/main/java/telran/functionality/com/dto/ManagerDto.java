package telran.functionality.com.dto;
/**
 * Class ManagerDto - to create object from Manager entity, which will be shown to user
 *
 * @author Olena Averchenko
 * */
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

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDto {

    private long id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Status status;
    private List<ClientDto> clients = new ArrayList<>();
    private List<ProductDto> products = new ArrayList<>();
}