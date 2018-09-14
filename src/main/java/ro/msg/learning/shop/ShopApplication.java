package ro.msg.learning.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ShopApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(ShopApplication.class);
        app.run();
    }

}
