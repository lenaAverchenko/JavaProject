package telran.functionality.com.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "products")
@NoArgsConstructor
@ToString
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Manager manager;
    private String name;
    private int status;
    private int currencyCode;
    private double interestRate;
    private int limitValue;
    private final Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt;

    public Product(long id, Manager manager, String name, int status, int limitValue) {
        this.id = id;
        this.manager = manager;
        this.name = name;
        this.status = status;
        this.limitValue = limitValue;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

}
