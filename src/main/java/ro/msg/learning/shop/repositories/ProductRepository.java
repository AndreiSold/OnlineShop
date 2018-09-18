package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.msg.learning.shop.entities.Product;

import java.util.List;

//At use you just need to autowire it
public interface ProductRepository extends JpaRepository<Product, Integer> {

//    Spring way ( not working this way )
//    List<Product> findAllBySuppliersLessThanEqualAndSuppliersGreaterThanEqualAndProductCategoriesLessThanEqualAndProductCategoriesGreaterThanEqual(int supplierLessThanEqual, int supplierGreaterThanEqual, int productCatLessThanEqual, int productCatGreaterThanEqual);
//
//    Put the spring query in a method with a shorter name
//    default List<Product> findAllBySupplierCountAndProductCategoryCount(int supplierCount, int productCategoryCount) {
//        return this.findAllBySuppliersLessThanEqualAndSuppliersGreaterThanEqualAndProductCategoriesLessThanEqualAndProductCategoriesGreaterThanEqual(supplierCount, supplierCount, productCategoryCount, productCategoryCount);
//    }

    //JPQL way
    @Query("select product from Product product where size(product.suppliers) = :supplierCount and size(product.productCategories) = :productCategoryCount")
    List<Product> findAllBySupplierCountAndProductCategoryCount2(@Param("supplierCount") int supplierCount, @Param("productCategoryCount") int productCategoryCount);

    //Implement needed queries here

}
