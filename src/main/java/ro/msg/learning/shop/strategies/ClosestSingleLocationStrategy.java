package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.msg.learning.shop.dtos.distance.DistanceApiResponseDto;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.DistanceCalculatorService;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ClosestSingleLocationStrategy implements SelectionStrategy {

    private final LocationRepository locationRepository;
    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StockRepository stockRepository;
    private final DistanceCalculatorService distanceCalculatorService;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList, Address address) {

        if (orderDetailList.isEmpty()) {
            log.error("The order details list is empty!");
            throw new OrderDetailsListEmptyException("You must give information about the order details!");
        }

        List<Location> shippedFrom = getLocationsThatHaveAllProducts(orderDetailList);

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationNonexistentException("No more details available!");
        }

        String destinationCity = address.getCity();
        String destinationCountry = address.getCountry();

        Map<Location, Double> resultMap = getOnlyLocationsThatCanBeReached(shippedFrom, destinationCity, destinationCountry);

        if (resultMap.isEmpty()) {
            log.error("No suitable location found to deliver all items on road!");
            throw new LocationNotFoundException(-1, "No suitable location found to deliver all items on road!");
        }

        Location chosenLocation = getLocationWithShortestDistance(resultMap);

        updateStocksFromLocationThatHaveCorrespondingOrderDetails(chosenLocation, orderDetailList);

        return strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(chosenLocation, orderDetailList);
    }

    private List<Location> getLocationsThatHaveAllProducts(List<OrderDetail> orderDetailList) {
        List<Location> shippedFrom = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            if (shippedFrom.isEmpty()) {
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            } else {
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            }
        });

        return shippedFrom;
    }

    private Location getLocationWithShortestDistance(Map<Location, Double> resultMap) {
        Double minDistance = 0D;
        Location chosenLocation = null;

        Iterator it = resultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Location location = (Location) pair.getKey();
            Double distance = (Double) pair.getValue();

            if ((minDistance == 0D) || (distance < minDistance)) {
                minDistance = distance;
                chosenLocation = location;
            }

            it.remove();
        }

        return chosenLocation;
    }

    private Map<Location, Double> getOnlyLocationsThatCanBeReached(List<Location> shippedFrom, String destinationCity, String destinationCountry) {
        Map<Location, Double> resultMap = new HashMap<>();

        for (Location location : shippedFrom) {
            DistanceApiResponseDto distanceResult = distanceCalculatorService.getDistanceApiResultBetweenTwoCities(location.getAddress().getCity(), location.getAddress().getCountry(), destinationCity, destinationCountry);

            if (distanceResult.getRows().get(0).getElements().get(0).getStatus().equals("OK")) {
                String distanceResultString = distanceResult.getRows().get(0).getElements().get(0).getDistance().getValue();
                resultMap.put(location, Double.valueOf(distanceResultString));
            }
        }

        return resultMap;
    }

    private void updateStocksFromLocationThatHaveCorrespondingOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        orderDetailList.stream().forEach(orderDetail -> {
            Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());

            if (stock == null) {
                log.error("No stock found to be updated!");
                throw new StockNotFoundException("No stock found after location abd order details list!");
            }

            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            stockRepository.save(stock);
        });
    }

}
