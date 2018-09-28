package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SingleLocationStrategy implements SelectionStrategy {

    private final LocationRepository locationRepository;
    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StockRepository stockRepository;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList) {

        if (orderDetailList.isEmpty()) {
            log.error("The order details list is empty!");
            throw new OrderDetailsListEmptyException("You must give information about the order details!");
        }

        List<Location> shippedFrom = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            if (shippedFrom.isEmpty()) {
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            } else {
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            }
        });

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationNonexistentException("No more details available!");
        }

        Location chosenLocation = shippedFrom.get(0);

        updateStocksFromLocationThatHaveCorrespondingOrderDetails(chosenLocation, orderDetailList);

        return strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(chosenLocation, orderDetailList);
    }

    private void updateStocksFromLocationThatHaveCorrespondingOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        orderDetailList.stream().forEach(orderDetail -> {
            Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            stockRepository.save(stock);
        });
    }

}
