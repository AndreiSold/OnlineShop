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
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.DistanceCalculatorService;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;

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

        List<Location> shippedFrom = new ArrayList<>();

        orderDetailList.stream().forEach(orderDetail -> {
            if (shippedFrom.isEmpty()) {
                shippedFrom.addAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            } else {
                shippedFrom.retainAll(locationRepository.findAllByStockQuantityAndProduct(orderDetail.getQuantity(), orderDetail.getProduct()));
            }
        });

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationNonexistentException("No more details available!");
        }

        String destinationCity = address.getCity();
        String destinationCountry = address.getCountry();

        int place = 0;
        boolean shipmentCanBeMade = false;
        Location chosenLocation = null;
        Double minDistance = 0D;

        while ((!shipmentCanBeMade) && (place < shippedFrom.size())) {
            DistanceApiResponseDto distanceResult = distanceCalculatorService.getDistanceApiResultBetweenTwoCities(shippedFrom.get(place).getAddress().getCity(), shippedFrom.get(place).getAddress().getCountry(), destinationCity, destinationCountry);
            if (distanceResult.getRows().get(0).getElements().get(0).getStatus().equals("ZERO_RESULTS")) {
                place++;
            } else {
                shipmentCanBeMade = true;
                chosenLocation = shippedFrom.get(place);

                String distanceString = distanceResult.getRows().get(0).getElements().get(0).getDistance().getText();
                String[] distanceStringRupture = distanceString.split(" ");

                minDistance = Double.valueOf(distanceStringRupture[0].replaceAll(",", ""));
            }
        }

        if (shipmentCanBeMade) {
            for (int i = place + 1; i < shippedFrom.size(); i++) {
                DistanceApiResponseDto distanceResult = distanceCalculatorService.getDistanceApiResultBetweenTwoCities(shippedFrom.get(i).getAddress().getCity(), shippedFrom.get(i).getAddress().getCountry(), destinationCity, destinationCountry);

                if (!distanceResult.getRows().get(0).getElements().get(0).getStatus().equals("ZERO_RESULTS")) {

                    String distanceString = distanceResult.getRows().get(0).getElements().get(0).getDistance().getText();
                    String[] distanceStringRupture = distanceString.split(" ");

                    Double toBeMinDistance = Double.valueOf(distanceStringRupture[0].replaceAll(",", ""));

                    if (toBeMinDistance < minDistance) {
                        minDistance = toBeMinDistance;
                        chosenLocation = shippedFrom.get(i);
                    }
                }
            }
        }

        if (chosenLocation == null) {
            log.error("No suitable location found to deliver all items on road!");
            throw new LocationNotFoundException(-1, "No suitable location found to deliver all items on road!");
        }

        updateStocksFromLocationThatHaveCorrespondingOrderDetails(chosenLocation, orderDetailList);

        return strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(chosenLocation, orderDetailList);
    }

    private void updateStocksFromLocationThatHaveCorrespondingOrderDetails(Location finalLocation, List<OrderDetail> orderDetailList) {

        orderDetailList.stream().forEach(orderDetail -> {
            Stock stock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(finalLocation, orderDetail.getProduct(), orderDetail.getQuantity());
            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            stockRepository.save(stock);
        });
    }

}
