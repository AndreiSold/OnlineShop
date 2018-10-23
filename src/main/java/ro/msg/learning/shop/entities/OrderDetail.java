package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "quantity"})
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    private Product product;

    @JsonIgnore
    @ManyToOne
    private Order order;

    private Integer quantity;

    @ManyToOne
    private Location location;

    public OrderDetail(OrderDetail orderDetail) {
        this.id = orderDetail.getQuantity();
        this.order = orderDetail.getOrder();
        this.quantity = orderDetail.getQuantity();
        this.product = orderDetail.getProduct();
        this.location = orderDetail.getLocation();
    }

}
