package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class CustomerDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private List<Role> roles;
    private List<Order> orders;
}
