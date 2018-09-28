package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    Stock findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(Location location, Product product, int quantity);

    @Query("select stock from Stock stock where stock.location = ?1")
    List<Stock> getAllStocksByLocation(Location location);
}
