package ro.msg.learning.shop.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.SuitableLocationInexistentException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SingleLocationStrategy implements SelectionStrategy {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList) {

        List<Location> shippedFrom = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetailList) {
            if (shippedFrom.isEmpty())
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            else
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
        }

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationInexistentException();
        }

        saveOrderDetails(shippedFrom.get(0), orderDetailList);

        return createStrategyWrapperList(shippedFrom.get(0), orderDetailList);
    }

    private void saveOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        for (OrderDetail orderDetail : orderDetailList) {
            Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            stockRepository.save(stock);
        }
    }

    private List<StrategyWrapper> createStrategyWrapperList(Location finalLocation, List<OrderDetail> orderDetailList) {

        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetailList) {
            StrategyWrapper strategyWrapper = new StrategyWrapper(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            strategyWrapperList.add(strategyWrapper);
        }

        return strategyWrapperList;
    }
}
