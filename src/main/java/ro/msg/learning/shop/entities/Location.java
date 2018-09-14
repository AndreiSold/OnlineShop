package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString(exclude = "orders")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Stock> stocks;

    @JsonIgnore
    @ManyToMany(mappedBy = "shippedFrom")
    private List<Order> orders;

    private String name;

    @Embedded
    private Address address;
}
