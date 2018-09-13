package ro.msg.learning.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan({"ro.msg.learning.shop.embeddables"})
public class ShopApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(ShopApplication.class);
        app.run();
    }
}
