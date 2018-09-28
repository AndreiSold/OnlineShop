package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
