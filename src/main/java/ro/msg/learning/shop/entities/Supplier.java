package ro.msg.learning.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany
    @JoinTable(name = "products_suppliers",
        joinColumns = {@JoinColumn(name = "supplierId")},
        inverseJoinColumns = {@JoinColumn(name = "productId")})
    private List<Product> products;

    private String name;
}
