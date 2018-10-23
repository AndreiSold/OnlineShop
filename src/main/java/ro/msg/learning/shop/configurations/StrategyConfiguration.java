package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.enums.StrategyEnum;
import ro.msg.learning.shop.exceptions.StrategyNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.services.StrategyService;
import ro.msg.learning.shop.strategies.ClosestSingleLocationStrategy;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StrategyConfiguration {

    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StrategyService strategyService;

    @Value("${initial-strategy}")
    private String initialStrategy;

    @Bean
    public SelectionStrategy selectionStrategy() {
        switch (StrategyEnum.valueOf(initialStrategy)) {
            case SINGLE_LOCATION:
                return new SingleLocationStrategy(strategyWrapperMapper, strategyService);
            case MULTIPLE_LOCATIONS:
                return new MultipleLocationsStrategy(strategyWrapperMapper, strategyService);
            case CLOSEST_SINGLE_LOCATION:
                return new ClosestSingleLocationStrategy(strategyWrapperMapper, strategyService);
            default:
                log.error("Given selection strategy does not exist! Given strategy: " + initialStrategy);

                StringBuilder existingStrategies = new StringBuilder();

                for (StrategyEnum strategy : StrategyEnum.values()) {
                    existingStrategies.append(strategy.name()).append(" ");
                }

                throw new StrategyNonexistentException(initialStrategy, "Existing strategies for the moment: " + existingStrategies);
        }
    }
}
