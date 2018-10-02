package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.exceptions.StrategyNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
@EnableAutoConfiguration
@ComponentScan("ro.msg.learning.shop.strategies")
@Slf4j
@RequiredArgsConstructor
public class StrategyConfiguration {

    private final LocationRepository locationRepository;
    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StockRepository stockRepository;

    @Value("${initial-strategy}")
    private String initial;

    @Bean
    public SelectionStrategy selectionStrategy() {
        switch (initial) {
            case ("singleLocation"):
                return new SingleLocationStrategy(locationRepository, strategyWrapperMapper, stockRepository);
            case ("multipleLoccation"):
                return new MultipleLocationsStrategy();
            default:
                log.error("Given selection strategy does not exist! Given strategy: " + initial);
                throw new StrategyNonexistentException(initial, "Existing strategies for the moment: singleLocation, multipleLocation");
        }
    }
}
