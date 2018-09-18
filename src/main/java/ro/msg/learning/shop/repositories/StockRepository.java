package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {

//    @Query("select product from Product product where size(product.suppliers) = :supplierCount and size(product.productCategories) = :productCategoryCount")
//    List<Location> findAllBySupplierCountAndProductCategoryCount2(@Param("supplierCount") int supplierCount, @Param("productCategoryCount") int productCategoryCount);

    Stock findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(Location location, Product product, int quantity);
}
