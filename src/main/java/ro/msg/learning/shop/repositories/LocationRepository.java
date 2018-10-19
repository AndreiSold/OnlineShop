package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Revenue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select stock.location from Stock stock where stock.quantity >= ?1 and stock.product = ?2")
    List<Location> findAllByStockQuantityAndProduct(int quantity, Product product);

    @Query("select stock.location from Stock stock where stock.product = ?1 and stock.quantity > 0")
    List<Location> findAllByStocksContainsProductWithPositiveQuantity(Product product);

    @Query("select stock.quantity from Stock stock where stock.product = ?1 and stock.location = ?2")
    Optional<Integer> getQuantityOfProductInStockFromLocation(Product product, Location location);

    @Query(value = "select orderDetail.location,sum(orderDetail.product.price * orderDetail.quantity), orderDetail.order.timestamp from OrderDetail orderDetail where orderDetail.order.timestamp >= ?1 and orderDetail.order.timestamp <= ?2 group by orderDetail.location")
    List<Object[]> getDailyRevenuesAsObjectsQuery(LocalDateTime afterDateTime, LocalDateTime beforeDateTime);

    default List<Revenue> createDailyRevenues() {
        LocalDateTime afterDateTime = LocalDateTime.now().minusHours(LocalDateTime.now().getHour()).minusMinutes(LocalDateTime.now().getMinute())
            .minusSeconds(LocalDateTime.now().getSecond()).minusNanos(LocalDateTime.now().getNano());

        LocalDateTime beforeDateTime = LocalDateTime.now();

        List<Object[]> revenuesAsObjects = getDailyRevenuesAsObjectsQuery(afterDateTime, beforeDateTime);

        List<Revenue> createdRevenues = new ArrayList<>();

        revenuesAsObjects.parallelStream().forEach(revenueObject ->
            createdRevenues.add(Revenue.builder().location((Location) revenueObject[0])
                .sum((Double) revenueObject[1])
                .timestamp((LocalDateTime) revenueObject[2])
                .build())
        );

        return createdRevenues;
    }
}
