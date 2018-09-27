package ro.msg.learning.shop.updates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.StockRepository;

import java.util.List;

@Component
public class StockUpdates {

    @Autowired
    private StockRepository stockRepository;

    public void updateStocksFromLocationThatHaveCorrespondingOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        orderDetailList.stream().forEach(orderDetail -> {
            Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            stockRepository.save(stock);
        });
    }
}
