package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, quantity);
    }

}
