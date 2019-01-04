package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getById(Integer orderId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    List<Order> getAllOrdersOfCustomerById(Integer customerId);

    @Query("SELECT o FROM Order o WHERE o.customer.username = ?1")
    List<Order> getAllOrdersOfCustomerByUsername(String customerUsername);
}
