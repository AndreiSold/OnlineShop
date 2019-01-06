package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"suppliers", "stocks", "orderDetails"})
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private Double weight;

    @JsonIgnore
    private Blob image;

    @ManyToOne
    private ProductCategory category;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Supplier> suppliers;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Stock> stocks;

    @JsonIgnore
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails;

}
