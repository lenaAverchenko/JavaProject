package telran.functionality.com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "agreements")
@NoArgsConstructor
@ToString
@Data
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private double interestRate;
    @Pattern(regexp = "[012]", message = "The field status must be either 0 or 1")
    private int status;
    @Positive
    private double sum;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt;

    public Agreement(Account account, Product product, double interestRate, double sum, Timestamp updatedAt) {
        this.account = account;
        this.product = product;
        this.interestRate = interestRate;
        this.status = 1;
        this.sum = sum;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
