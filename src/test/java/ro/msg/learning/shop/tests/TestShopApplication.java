package ro.msg.learning.shop.tests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan({"ro.msg.learning.shop", "ro.msg.learning.shop.tests"})
public class TestShopApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestShopApplication.class);
        app.run(args);
    }

}
