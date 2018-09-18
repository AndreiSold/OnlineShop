package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.wrappers.StrategyWrapper;
import ro.msg.learning.shop.entities.OrderDetail;

import java.util.List;

public interface SelectionStrategy {
    List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList);
}
