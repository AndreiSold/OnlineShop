package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select stock.location from Stock stock where stock.quantity >= ?1 and stock.product = ?2")
    List<Location> findAllByStockQuantityAndProduct(int quantity, Product product);

    @Query("select stock.location from Stock stock where stock.product = ?1")
    List<Location> findAllByStocksContainsProduct(Product product);

    @Query("select stock.quantity from Stock stock where stock.product = ?1 and stock.location = ?2")
    Optional<Integer> getQuantityOfProductAtLocation(Product product, Location location);
}
