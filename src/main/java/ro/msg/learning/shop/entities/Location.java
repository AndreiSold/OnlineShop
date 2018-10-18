package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString(exclude = {"stocks", "orders", "orderDetails"})
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    public Location(Integer id, List<Stock> stocks, List<OrderDetail> orderDetailList, List<Order> orders, String name, Address address) {
        this.id = id;
        this.stocks = stocks;
        this.orderDetails = orderDetailList;
        this.orders = orders;
        this.name = name;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Stock> stocks;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    @ManyToMany(mappedBy = "shippedFrom")
    private List<Order> orders;

    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Revenue> revenue;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
