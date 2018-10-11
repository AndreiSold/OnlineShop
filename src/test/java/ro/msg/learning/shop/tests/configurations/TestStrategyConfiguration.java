package ro.msg.learning.shop.tests.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.DistanceCalculatorService;
import ro.msg.learning.shop.strategies.ClosestSingleLocationStrategy;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
public class TestStrategyConfiguration {

    private final LocationRepository locationRepository;
    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StockRepository stockRepository;
    private final DistanceCalculatorService distanceCalculatorService;

    @Bean
    public SingleLocationStrategy singleLocationStrategy() {
        return new SingleLocationStrategy(locationRepository, strategyWrapperMapper, stockRepository);
    }

    @Bean
    public ClosestSingleLocationStrategy closestSingleLocationStrategy() {
        return new ClosestSingleLocationStrategy(locationRepository, strategyWrapperMapper, stockRepository, distanceCalculatorService);
    }

    @Bean
    public MultipleLocationsStrategy multipleLocationsStrategy() {
        return new MultipleLocationsStrategy();
    }

}
