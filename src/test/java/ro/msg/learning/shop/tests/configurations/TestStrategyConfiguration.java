package ro.msg.learning.shop.tests.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.services.StrategyCreationService;
import ro.msg.learning.shop.strategies.ClosestSingleLocationStrategy;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
public class TestStrategyConfiguration {

    private final StrategyCreationService strategyCreationService;
    private final StrategyWrapperMapper strategyWrapperMapper;

    @Bean
    public SingleLocationStrategy singleLocationStrategy() {
        return new SingleLocationStrategy(strategyWrapperMapper, strategyCreationService);
    }

    @Bean
    public ClosestSingleLocationStrategy closestSingleLocationStrategy() {
        return new ClosestSingleLocationStrategy(strategyWrapperMapper, strategyCreationService);
    }

    @Bean
    public MultipleLocationsStrategy multipleLocationsStrategy() {
        return new MultipleLocationsStrategy(strategyWrapperMapper, strategyCreationService);
    }

}
