package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
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

}
