package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.services.StrategyService;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ClosestSingleLocationStrategy implements SelectionStrategy {

    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StrategyService strategyService;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList, Address address) {

        if (orderDetailList.isEmpty()) {
            log.error("The order details list is empty!");
            throw new OrderDetailsListEmptyException("You must give information about the order details!");
        }

        List<Location> shippedFrom = strategyService.getLocationsThatHaveAllProducts(orderDetailList);

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationNonexistentException("No more details available!");
        }

        String destinationCity = address.getCity();
        String destinationCountry = address.getCountry();

        Map<Location, Double> resultMap = strategyService.getOnlyLocationsThatCanBeReached(shippedFrom, destinationCity, destinationCountry);

        if (resultMap.isEmpty()) {
            log.error("No suitable location found to deliver all items on road!");
            throw new LocationNotFoundException(-1, "No suitable location found to deliver all items on road!");
        }

        Location chosenLocation = strategyService.getLocationWithShortestDistance(resultMap);

        final List<StrategyWrapper> strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListForSingleLocationStrategy(chosenLocation, orderDetailList);

        strategyService.updateStocksFromLocationThatHaveCorrespondingOrderDetails(chosenLocation, orderDetailList);

        return strategyWrapperList;
    }


}
