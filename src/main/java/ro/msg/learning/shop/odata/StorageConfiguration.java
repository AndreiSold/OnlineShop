package ro.msg.learning.shop.odata;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.repositories.ProductRepository;

@RequiredArgsConstructor
@Configuration
public class StorageConfiguration {

    private final ProductRepository productRepository;

    @Bean
    public Storage storage() {
        return new Storage(productRepository);
    }
}
