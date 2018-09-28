package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.wrappers.StrategyWrapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class MultipleLocationsStrategy implements SelectionStrategy {

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList) {
        throw new NotImplementedException();
    }
}
