package ro.msg.learning.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private BigInteger price;

    private Double weight;

    @ManyToMany
    @JoinTable(name = "products_categories",
        joinColumns = {@JoinColumn(name = "productId")},
        inverseJoinColumns = {@JoinColumn(name = "productCategoryId")})
    private List<ProductCategory> productCategories;

    @ManyToMany
    @JoinTable(name = "products_suppliers",
        joinColumns = {@JoinColumn(name = "productId")},
        inverseJoinColumns = {@JoinColumn(name = "supplierId")})
    private List<Supplier> suppliers;

    @OneToMany(mappedBy = "product")
    private List<Stock> stocks;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;
}
