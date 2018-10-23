package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.distance.DistanceResponseDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.StockNotFoundException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.utilities.distance.DistanceCalculator;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StrategyService {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final DistanceCalculator distanceCalculator;

    public List<Location> getLocationsThatHaveAllProducts(List<OrderDetail> orderDetailList) {

        List<Location> shippedFrom = new ArrayList<>();

        orderDetailList.forEach(orderDetail -> {
            if (shippedFrom.isEmpty()) {
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            } else {
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            }
        });

        return shippedFrom;
    }

    public Location getLocationWithShortestDistance(Map<Location, Double> resultMap) {

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

    public Map<Location, Double> getOnlyLocationsThatCanBeReached(List<Location> shippedFrom, String destinationCity, String destinationCountry) {

        Map<Location, Double> resultMap = new HashMap<>();

        for (Location location : shippedFrom) {
            DistanceResponseDto distanceResult = distanceCalculator.getDistanceResponseBetweenTwoCities(location.getAddress().getCity(), location.getAddress().getCountry(), destinationCity, destinationCountry);

            if (distanceResult.getRows().get(0).getElements().get(0).getStatus().equals("OK")) {
                String distanceResultString = distanceResult.getRows().get(0).getElements().get(0).getDistance().getValue();
                resultMap.put(location, Double.valueOf(distanceResultString));
            }
        }

        return resultMap;
    }

    public Double getDistanceBetweenTwoLocations(Location firstLocation, Location secondLocation) {

        DistanceResponseDto distanceResult = distanceCalculator.getDistanceResponseBetweenTwoCities(firstLocation.getAddress().getCity(), firstLocation.getAddress().getCountry(), secondLocation.getAddress().getCity(), secondLocation.getAddress().getCountry());

        if (distanceResult.getRows().get(0).getElements().get(0).getStatus().equals("OK")) {
            String distanceResultString = distanceResult.getRows().get(0).getElements().get(0).getDistance().getValue();
            return Double.valueOf(distanceResultString);
        }

        return 0D;
    }

    public void updateStocksFromLocationThatHaveCorrespondingOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

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

    public List<Location> getAllNoDoubleLocationsThatHaveAtLeastOneProduct(List<OrderDetail> orderDetailList) {

        List<Location> shippedFrom = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail ->
            shippedFrom.addAll(locationRepository.findAllByStocksContainsProductWithPositiveQuantity(orderDetail.getProduct()))
        );

        Set<Location> shippedFromNoDuplicates = new HashSet<>();
        shippedFromNoDuplicates.addAll(shippedFrom);
        shippedFrom.clear();
        shippedFrom.addAll(shippedFromNoDuplicates);

        return shippedFrom;
    }

    public List<Location> getTheLocationListFromMapResult(Map<Location, Double> resultMap) {

        List<Location> locations = new ArrayList<>();

        Iterator it = resultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Location location = (Location) pair.getKey();

            locations.add(location);

            it.remove();
        }

        return locations;
    }

    public Optional<Integer> getQuantityOfProductAtLocation(Product product, Location location) {
        return locationRepository.getQuantityOfProductInStockFromLocation(product, location);
    }

    public void updateStockForStrategyWrapper(StrategyWrapper strategyWrapper) {
        Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(strategyWrapper.getLocation(), strategyWrapper.getProduct(), strategyWrapper.getQuantity());

        stock.setQuantity(stock.getQuantity() - strategyWrapper.getQuantity());

        stockRepository.save(stock);
    }
}
