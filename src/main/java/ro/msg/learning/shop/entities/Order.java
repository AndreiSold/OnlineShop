package ro.msg.learning.shop.entities;

import lombok.Data;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "order_")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany
    @JoinTable(name = "orders_locations",
        joinColumns = {@JoinColumn(name = "orderId")},
        inverseJoinColumns = {@JoinColumn(name = "locationId")})
    private List<Location> shippedFrom;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @Embedded
    private Address address;
}
