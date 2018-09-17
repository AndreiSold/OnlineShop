package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "order_")
@ToString(exclude = "shippedFrom")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnore
    @ManyToMany
    private List<Location> shippedFrom;

    @ManyToOne
    private Customer customer;

    //@JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @Embedded
    private Address address;
}
