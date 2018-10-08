package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"orders", "roles"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

    private String password;
}
