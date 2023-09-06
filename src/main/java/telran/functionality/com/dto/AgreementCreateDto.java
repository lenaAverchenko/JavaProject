package telran.functionality.com.dto;
/**
 * Class AgreementCreateDto - provided by user object, given to convert into Agreement entity
 *
 * @author Olena Averchenko
 * */
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementCreateDto {

    private UUID accountId;
    private long productId;
    private double interestRate;
    private double sum;
}
