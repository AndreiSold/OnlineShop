package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.MultipleStrategyResultDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationPassedAsNullException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StrategyCreationService;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyWrapperMapper {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final StrategyCreationService strategyCreationService;

    public List<StrategyWrapper> createStrategyWrapperListAndUpdateStocks(Location finalLocation, List<OrderDetail> orderDetailList) {

        if (finalLocation == null) {
            log.error("Null location given as parameter!");
            throw new LocationPassedAsNullException("Location passed must not be null!");
        }

        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            StrategyWrapper strategyWrapper = new StrategyWrapper(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            strategyWrapperList.add(strategyWrapper);
        });

        //update stocks here
        strategyCreationService.updateStocksFromLocationThatHaveCorrespondingOrderDetails(finalLocation, orderDetailList);

        return strategyWrapperList;
    }

    public List<StrategyWrapper> createStrategyWrapperListForMultipleLocationStrategy(MultipleStrategyResultDto multipleStrategyResultDto, List<OrderDetail> orderDetailList, List<Location> locationList) {

        //get the first (last index) city for the courier to leave from
        int lastCityIndex = biggestValueInArray(multipleStrategyResultDto.getCitiesPath());

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

                        Stock stock = stockRepository.findStockThatHasProductFromLocation(orderDetail.getProduct(), location);

                        if (quantity > orderDetail.getQuantity()) {
                            stock.setQuantity(quantity - orderDetail.getQuantity());

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

                            stock.setQuantity(0);
                        }

                        stockRepository.save(stock);
                    }
                }
            }


        }

        //when you reach the city with index one you can return the strategy wrapper list but also check if the order details have quantities 0
        return strategyWrapperList;
    }

    private int biggestValueInArray(Integer[] array) {

        if (array.length == 0) {
            log.error("Given path array is empty!");
            throw new EmptyStackException();
        }

        int maxValue = array[0];

        for (Integer integer : array) {
            if (integer > maxValue) {
                maxValue = integer;
            }
        }

        return maxValue;
    }
}
