package telran.functionality.com.dto;

import lombok.*;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDto {

    private long managerId;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
