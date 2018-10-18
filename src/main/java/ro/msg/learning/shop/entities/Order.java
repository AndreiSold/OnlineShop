package ro.msg.learning.shop.entities;

import lombok.*;
import ro.msg.learning.shop.embeddables.Address;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "order_")
@ToString(exclude = {"orderDetails", "shippedFrom"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToMany
    private List<Location> shippedFrom;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @Embedded
    private Address address;

    private LocalDateTime timestamp;
}