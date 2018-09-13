package ro.msg.learning.shop.entities;

import lombok.Data;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(mappedBy = "location")
    private List<Stock> stocks;

    @ManyToMany
    @JoinTable(name = "orders_locations",
        joinColumns = {@JoinColumn(name = "locationId")},
        inverseJoinColumns = {@JoinColumn(name = "orderId")})
    private List<Order> orders;

    private String name;

    @Embedded
    private Address address;
}
