package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.services.StrategyCreationService;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SingleLocationStrategy implements SelectionStrategy {

    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StrategyCreationService strategyCreationService;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList, Address address) {

        if (orderDetailList.isEmpty()) {
            log.error("The order details list is empty!");
            throw new OrderDetailsListEmptyException("You must give information about the order details!");
        }

        List<Location> shippedFrom = strategyCreationService.getLocationsThatHaveAllProducts(orderDetailList);

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationNonexistentException("No more details available!");
        }

        Location chosenLocation = shippedFrom.get(0);

        return strategyWrapperMapper.createStrategyWrapperListAndUpdateStocks(chosenLocation, orderDetailList);
    }

}
