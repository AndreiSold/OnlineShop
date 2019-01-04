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

    @Query("select sum(orderDetail.quantity) from OrderDetail orderDetail where orderDetail.product.id = ?1 and orderDetail.order.timestamp >= ?2 and orderDetail.order.timestamp <= ?3")
    Integer getQuantitySoldInLastMonthByProductIdQuery(int productId, LocalDateTime afterDateEquals, LocalDateTime beforeDateEquals);

    default Integer getQuantitySoldInLastMonthByProductId(int productId) {
        val afterDateEquals = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfMonth());
        val beforeDateEquals = afterDateEquals.plusMonths(1).minusSeconds(1);

        return getQuantitySoldInLastMonthByProductIdQuery(productId, afterDateEquals, beforeDateEquals);
    }

    Product getById(Integer id);

    @Query(value = "SELECT * FROM Product p WHERE p.price >= ?1 AND p.price <= ?2", nativeQuery = true)
    List<Product> getProductsInRange(Double startPrice, Double endPrice);

    @Query(value = "SELECT * FROM Product p WHERE p.category_id = ?1", nativeQuery = true)
    List<Product> getProductsFromCategory(Integer categoryId);

    @Query(value = "SELECT * FROM Product p WHERE p.price >= ?1 and p.price <= ?2 and p.category_id = ?3", nativeQuery = true)
    List<Product> getProductsInRangeFromCategory(Double startPrice, Double endPrice, Integer categoryId);

    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE CONCAT('%', ?1, '%')")
    List<Product> getProductsThatContainString(String string);
}

