package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.MultipleStrategyResultDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.LocationPassedAsNullException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyWrapperMapper {

    private final LocationRepository locationRepository;

    public List<StrategyWrapper> createStrategyWrapperListForSingleLocationStrategy(Location finalLocation, List<OrderDetail> orderDetailList) {

        if (finalLocation == null) {
            log.error("Null location given as parameter!");
            throw new LocationPassedAsNullException("Location passed must not be null!");
        }

        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        orderDetailList.forEach(orderDetail -> {
            StrategyWrapper strategyWrapper = new StrategyWrapper(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            strategyWrapperList.add(strategyWrapper);
        });

        return strategyWrapperList;
    }

    public List<StrategyWrapper> createStrategyWrapperListForMultipleLocationStrategy(MultipleStrategyResultDto multipleStrategyResultDto, List<OrderDetail> orderDetailList, List<Location> locationList) {

        Comparator<Integer> integerComparator = Comparator.comparingInt(firstInteger -> firstInteger);

        Optional<Integer> lastCityIndexOptional = Arrays.stream(multipleStrategyResultDto.getCitiesPath()).max(integerComparator);
        int lastCityIndex = lastCityIndexOptional.get();

        //create strategy wrapper for that first city than reduce the order details and go to the next city index
        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        for (int value = lastCityIndex; value > 1; value--) {

            int position;
            for (position = 0; position < multipleStrategyResultDto.getCitiesPath().length; position++) {
                if (multipleStrategyResultDto.getCitiesPath()[position] == value) {
                    break;
                }
            }

            Location location = locationList.get(position);

            for (OrderDetail orderDetail : orderDetailList) {
                if (orderDetail.getQuantity() > 0) {
                    Optional<Integer> quantityOptional = locationRepository.getQuantityOfProductInStockFromLocation(orderDetail.getProduct(), location);

                    int quantity = 0;
                    if (quantityOptional.isPresent()) {
                        quantity = quantityOptional.get();
                    }

                    if (quantityOptional.isPresent() && quantity != 0) {

                        if (quantity > orderDetail.getQuantity()) {
                            strategyWrapperList.add(StrategyWrapper.builder()
                                .location(location)
                                .product(orderDetail.getProduct())
                                .quantity(orderDetail.getQuantity())
                                .build());

                            orderDetail.setQuantity(0);
                        } else {
                            orderDetail.setQuantity(orderDetail.getQuantity() - quantity);

                            strategyWrapperList.add(StrategyWrapper.builder()
                                .location(location)
                                .product(orderDetail.getProduct())
                                .quantity(quantity)
                                .build());
                        }
                    }
                }
            }

        }

        //when you reach the city with index one you can return the strategy wrapper list but also check if the order details have quantities 0
        return strategyWrapperList;
    }

}
