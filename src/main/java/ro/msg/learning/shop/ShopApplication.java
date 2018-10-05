package ro.msg.learning.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Slf4j
@SpringBootApplication
@EntityScan(basePackageClasses = {Jsr310JpaConverters.class, ShopApplication.class})
public class ShopApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(ShopApplication.class);
        app.run();
    }

}
