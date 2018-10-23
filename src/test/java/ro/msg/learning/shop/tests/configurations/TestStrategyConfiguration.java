package ro.msg.learning.shop.tests.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.services.StrategyService;
import ro.msg.learning.shop.strategies.ClosestSingleLocationStrategy;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
public class TestStrategyConfiguration {

    private final StrategyService strategyService;
    private final StrategyWrapperMapper strategyWrapperMapper;

    @Bean
    public SingleLocationStrategy singleLocationStrategy() {
        return new SingleLocationStrategy(strategyWrapperMapper, strategyService);
    }

    @Bean
    public ClosestSingleLocationStrategy closestSingleLocationStrategy() {
        return new ClosestSingleLocationStrategy(strategyWrapperMapper, strategyService);
    }

    @Bean
    public MultipleLocationsStrategy multipleLocationsStrategy() {
        return new MultipleLocationsStrategy(strategyWrapperMapper, strategyService);
    }

}
