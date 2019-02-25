package be.ordina.beershop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
public class Order {

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @Id
    private Long id;

    private String number;

    public Order() {
    }

    public Order(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
