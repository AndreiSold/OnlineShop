package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select stock.location from Stock stock where stock.quantity >= ?1 and stock.product = ?2")
    List<Location> findAllByStockQuantityAndProduct(int quantity, Product product);

    @Query("select stock from Stock stock where stock.location = ?1")
    List<Stock> getAllStocksByLocation(Location location);
}
