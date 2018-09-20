package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.ProductCategory;
import ro.msg.learning.shop.entities.Supplier;
import ro.msg.learning.shop.repositories.ProductCategoryRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.repositories.SupplierRepository;

import java.util.ArrayList;
import java.util.List;

//Testing services for now
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SupplierRepository supplierRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void saveNewProduct(String description, String name, Double price, Double weight) {

        Product newProduct = Product.builder()
            .description(description)
            .name(name)
            .price(price)
            .weight(weight)
            .build();

        ProductCategory productCategory = new ProductCategory();

        Supplier supplier = new Supplier();

        List<Product> productList = new ArrayList<>();
        productList.add(newProduct);

        List<Supplier> supplierList = new ArrayList<>();
        supplierList.add(supplier);

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);

        newProduct.setProductCategories(productCategoryList);
        newProduct.setSuppliers(supplierList);

        supplier.setProducts(productList);

        productCategory.setProducts(productList);

        productCategory.setDescription("descr");
        productCategory.setName("sadgsdagasd");

        supplier.setName("sdagsdagsda");

        productRepository.save(newProduct);
    }
}
