package ro.msg.learning.shop.odata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@RequiredArgsConstructor
@Configuration
public class StorageConfiguration {

//    private final ProductRepository productRepository;
//    private final DemoEdmProvider demoEdmProvider;

    @Bean
    public Storage storage() {
        return new Storage();
    }
}
