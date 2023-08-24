package telran.functionality.com.entity;


import lombok.*;
import telran.functionality.com.enums.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

import java.util.Date;

@Entity
@Table(name = "products")
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Manager manager;
    @NotBlank(message = "Field name is mandatory")
    private String name;

    private int status;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
    private double interestRate;

    private int limitValue;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

    public Product(Manager manager, String name, Currency currencyCode, double interestRate, int limitValue, Timestamp updatedAt) {
        this.manager = manager;
        this.name = name;
        this.status = 1;
        this.currencyCode = currencyCode;
        this.interestRate = interestRate;
        this.limitValue = limitValue;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
