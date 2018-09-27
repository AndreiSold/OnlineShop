package ro.msg.learning.shop.mappers;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class StrategyWrapperMapper {

    public List<StrategyWrapper> createStrategyWrapperListFromLocationAndOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            StrategyWrapper strategyWrapper = new StrategyWrapper(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            strategyWrapperList.add(strategyWrapper);
        });

        return strategyWrapperList;
    }
}
