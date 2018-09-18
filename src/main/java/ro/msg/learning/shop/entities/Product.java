package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"productCategories", "suppliers"})
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private Double weight;

    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductCategory> productCategories;

    @JsonIgnore
    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<Supplier> suppliers;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Stock> stocks;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

}
