package ro.msg.learning.shop.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;

    private Integer quantity;
}
