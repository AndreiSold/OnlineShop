package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByUsername(String username);

    @Query("SELECT c FROM Customer c WHERE c.username = ?1 AND c.password = ?2")
    Optional<Customer> findByUsernameAndPassword(String username, String password);
}
