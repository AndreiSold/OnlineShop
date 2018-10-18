package ro.msg.learning.shop.repositories;

import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select order from Order order where order.timestamp >= ?1 and order.timestamp <= ?2")
    List<Order> getAllOrdersFromCurrentDayQuery(LocalDateTime afterDateEquals, LocalDateTime beforeDateEquals);

    default List<Order> getAllOrdersFromCurrentDay() {
        val afterDateEquals = LocalDateTime.now().minusHours(LocalDateTime.now().getHour()).minusMinutes(LocalDateTime.now().getMinute()).minusSeconds(LocalDateTime.now().getSecond()).minusNanos(LocalDateTime.now().getNano());

        val beforeDateEquals = LocalDateTime.now();

        return getAllOrdersFromCurrentDayQuery(afterDateEquals, beforeDateEquals);
    }
}
