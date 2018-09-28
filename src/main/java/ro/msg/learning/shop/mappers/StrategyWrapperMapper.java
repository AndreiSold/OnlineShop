package ro.msg.learning.shop.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.LocationPassedAsNullException;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class StrategyWrapperMapper {


    public List<StrategyWrapper> createStrategyWrapperListFromLocationAndOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        if (finalLocation == null) {
            log.error("Null location given as parameter!");
            throw new LocationPassedAsNullException("Location passed must not be null!");
        }

        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            StrategyWrapper strategyWrapper = new StrategyWrapper(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            strategyWrapperList.add(strategyWrapper);
        });

        return strategyWrapperList;
    }
}
