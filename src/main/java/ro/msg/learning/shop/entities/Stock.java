package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;

    private Integer quantity;
}
