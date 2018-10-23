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
@ToString(exclude = {"stocks", "orders", "orderDetails", "revenues"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

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
    private List<Revenue> revenues;

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
