package ro.msg.learning.shop.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.SuitableLocationInexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.updates.StockUpdates;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SingleLocationStrategy implements SelectionStrategy {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private StockUpdates stockUpdates;
    @Autowired
    private StrategyWrapperMapper strategyWrapperMapper;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList) {

        if (orderDetailList.isEmpty()) {
            log.error("The order details list is empty!");
            throw new OrderDetailsListEmptyException("You must give information about the order details!");
        }

        List<Location> shippedFrom = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            if (shippedFrom.isEmpty())
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            else
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
        });

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationInexistentException("No more details available!");
        }

        Location chosenLocation = shippedFrom.get(0);

        stockUpdates.updateStocksFromLocationThatHaveCorrespondingOrderDetails(chosenLocation, orderDetailList);

        return strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(chosenLocation, orderDetailList);
    }

}
