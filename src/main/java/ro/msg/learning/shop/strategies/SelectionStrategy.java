package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.List;

public interface SelectionStrategy {

    List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList);
}
