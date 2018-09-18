package ro.msg.learning.shop.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.SuitableLocationInexistentException;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.wrappers.StrategyWrapper;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.repositories.LocationRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SingleLocationStrategy implements SelectionStrategy {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    StockRepository stockRepository;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList) {

        List<Location> shippedFrom = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetailList) {
            if (shippedFrom.isEmpty())
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            else
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
        }

        //Checking if the strategy found a suitable location else we throw an exception
        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationInexistentException();
        }

        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        Location finalLocation = shippedFrom.get(0);

        //here you must update the stocks taken from in this final location
        for (OrderDetail orderDetail : orderDetailList) {
            Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            stockRepository.save(stock);
        }

        for (OrderDetail orderDetail : orderDetailList) {
            StrategyWrapper strategyWrapper = new StrategyWrapper(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            strategyWrapperList.add(strategyWrapper);
        }

        return strategyWrapperList;
    }
}
