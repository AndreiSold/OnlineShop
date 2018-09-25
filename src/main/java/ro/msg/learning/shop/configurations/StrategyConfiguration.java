package ro.msg.learning.shop.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.exceptions.StrategyInexistentException;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
@EnableAutoConfiguration
@ComponentScan("ro.msg.learning.shop.strategies")
@Slf4j
public class StrategyConfiguration {

    @Value("${initial-strategy}")
    private String initialStrategy;

    @Bean
    public SelectionStrategy selectionStrategy() {
        switch (initialStrategy) {
            case ("singleLocation"):
                return new SingleLocationStrategy();
            case ("multipleLoccation"):
                return new MultipleLocationsStrategy();
            default:
                log.error("Given selection strategy does not exist! Given strategy: " + initialStrategy);
                throw new StrategyInexistentException(initialStrategy, "Existing strategies for the moment: singleLocation, multipleLocation");
        }
    }
}
