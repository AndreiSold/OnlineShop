package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.msg.learning.shop.entities.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select product from Product product where size(product.suppliers) = :supplierCount and size(product.productCategories) = :productCategoryCount")
    List<Product> findAllBySupplierCountAndProductCategoryCount2(@Param("supplierCount") int supplierCount, @Param("productCategoryCount") int productCategoryCount);
}
