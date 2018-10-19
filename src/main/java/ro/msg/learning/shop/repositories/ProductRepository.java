package ro.msg.learning.shop.repositories;

import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select distinct orderDetail.product from OrderDetail orderDetail where orderDetail.order.timestamp >= ?1 and orderDetail.order.timestamp <= ?2")
    List<Product> selectProductsPurchasedFromCurrentMonthQuery(LocalDateTime afterDateEquals, LocalDateTime beforeDateEquals);

    default List<Product> selectProductsPurchasedFromCurrentMonth() {
        val afterDateEquals = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfMonth());
        val beforeDateEquals = afterDateEquals.plusMonths(1).minusSeconds(1);

        return selectProductsPurchasedFromCurrentMonthQuery(afterDateEquals, beforeDateEquals);
    }
}

