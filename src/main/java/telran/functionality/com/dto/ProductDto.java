package telran.functionality.com.dto;
/**
 * Class ProductDto - to create object from Product entity, which will be shown to user
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

@Data
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    @Schema(description = "Product identifier",defaultValue = "1")
    private long id;

    @Schema(description = "Name of product", defaultValue = "Mortgage")
    private String name;

    @Schema(description = "Status of product",defaultValue = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Limit",defaultValue = "10000")
    private int limitValue;

    @Schema(description = "Manager",
            defaultValue = "{    \"firstName\": \"managerFirstUpdated\",\n" +
                           "    \"lastName\": \"ThroughPostmanUpdated\",\n" +
                           "    \"status\": \"ACTIVE\"\n}")
    private ManagerDto manager;

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", limitValue=" + limitValue +
                '}';
    }
}