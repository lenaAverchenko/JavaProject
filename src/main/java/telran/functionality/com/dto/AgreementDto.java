package telran.functionality.com.dto;
/**
 * Class AgreementDto - to create object from Agreement entity, which will be shown to user
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

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDto {

    private AccountDto accountDto;
    private ProductDto productDto;
    private double interestRate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private double sum;
}