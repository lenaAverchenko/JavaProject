package telran.functionality.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import telran.functionality.com.enums.Currency;

import javax.persistence.*;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreateDto {


    private long managerId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
    private double interestRate;
    private int limitValue;
}
