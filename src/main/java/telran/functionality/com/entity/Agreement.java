package telran.functionality.com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.functionality.com.enums.Status;

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

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private double interestRate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Positive
    private double sum;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

    public Agreement(Account account, Product product, double interestRate, double sum, Timestamp updatedAt) {
        this.account = account;
        this.product = product;
        this.interestRate = interestRate;
        this.status = Status.INACTIVE;
        this.sum = sum;
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
