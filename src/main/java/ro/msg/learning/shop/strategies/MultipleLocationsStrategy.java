package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.services.StrategyCreationService;
import ro.msg.learning.shop.wrappers.StrategyWrapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MultipleLocationsStrategy implements SelectionStrategy {

    private final StrategyWrapperMapper strategyWrapperMapper;
    private final StrategyCreationService strategyCreationService;

    @Override
    public List<StrategyWrapper> getStrategyResult(List<OrderDetail> orderDetailList, Address address) {

        if (orderDetailList.isEmpty()) {
            log.error("The order details list is empty!");
            throw new OrderDetailsListEmptyException("You must give information about the order details!");
        }

        List<Location> shippedFrom = strategyCreationService.getAllNoDoubleLocationsThatHaveAtLeastOneProduct(orderDetailList);

        if (shippedFrom.isEmpty()) {
            log.error("There isn't any location having all the products the customer ordered!");
            throw new SuitableLocationNonexistentException("No more details available!");
        }

        String destinationCity = address.getCity();
        String destinationCountry = address.getCountry();

        Map<Location, Double> resultMap = strategyCreationService.getOnlyLocationsThatCanBeReached(shippedFrom, destinationCity, destinationCountry);

        if (resultMap.isEmpty()) {
            log.error("No suitable location found to deliver all items on road!");
            throw new LocationNotFoundException(-1, "No suitable location found to deliver all items on road!");
        }

//        Location chosenLocation = strategyCreationService.getLocationWithShortestDistance(resultMap);
//
//        strategyCreationService.updateStocksFromLocationThatHaveCorrespondingOrderDetails(chosenLocation, orderDetailList);
//
//        return strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(chosenLocation, orderDetailList);

        Map<Location, Double> resultMapAux = new HashMap<>(resultMap);
        Double[][] mapGraph = createMapGraphForLocationsWithWantedProducts(resultMapAux);

        //TODO when getting the locations from the resultMap you must set the destination location as the FIRST ONE!!!!!
        Location destinationLocation = new Location(null, null, null, null, address);
        List<Location> locationList = new ArrayList<>();
        locationList.add(destinationLocation);
        locationList.addAll(strategyCreationService.getTheLocationListFromMapResult(resultMap));

        Integer[] citiesPath = new Integer[mapGraph.length];
        Arrays.fill(citiesPath, 0);
        citiesPath[0] = 1;

        Boolean[] visitedCities = new Boolean[mapGraph.length];
        Arrays.fill(visitedCities, false);
        visitedCities[0] = true;

        backt(0, 2, mapGraph, locationList, citiesPath, visitedCities, 0D, orderDetailList);

        throw new NotImplementedException();
    }

    private Double[][] createMapGraphForLocationsWithWantedProducts(Map<Location, Double> resultMap) {

        int size = resultMap.size() + 1;

        Double[][] mapGraph = new Double[size][size];
        mapGraph[0][0] = 0D;

        int firstIteratorPosition = 0;

        Iterator firstIterator = resultMap.entrySet().iterator();

        Map<Location, Double> resultMapNoChange = new HashMap<>(resultMap);

        while (firstIterator.hasNext()) {
            firstIteratorPosition++;
            Map.Entry pair = (Map.Entry) firstIterator.next();
            Double firstIteratorDistance = (Double) pair.getValue();
            Location firstIteratorLocation = (Location) pair.getKey();

            mapGraph[0][firstIteratorPosition] = firstIteratorDistance;
            mapGraph[firstIteratorPosition][0] = firstIteratorDistance;

            Map<Location, Double> resultMapAux = new HashMap<>(resultMapNoChange);
            Iterator secondIterator = resultMapAux.entrySet().iterator();

            int secondIteratorPosition = 0;
            while (secondIterator.hasNext()) {
                secondIteratorPosition++;

                Map.Entry secondPair = (Map.Entry) secondIterator.next();
                if (firstIteratorPosition == secondIteratorPosition) {
                    mapGraph[firstIteratorPosition][secondIteratorPosition] = 0D;
                } else {
                    Location secondIteratorLocation = (Location) secondPair.getKey();

                    Double distanceBetweenCities = strategyCreationService.getDistanceBetweenTwoLocations(firstIteratorLocation, secondIteratorLocation);

                    mapGraph[firstIteratorPosition][secondIteratorPosition] = distanceBetweenCities;
                    mapGraph[secondIteratorPosition][firstIteratorPosition] = distanceBetweenCities;
                }
                secondIterator.remove();
            }
            firstIterator.remove();
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(mapGraph[i][j] + "        ");
            }
            System.out.println("\n");
        }

        return mapGraph;
    }

    private void calculateAllTheRoads(Double[][] mapGraph) {


    }

    private boolean isSolution(List<OrderDetail> leftOrderDetails) {

        return leftOrderDetails.parallelStream().filter(orderDetail -> {
            if (orderDetail.getQuantity() != 0) {
                return true;
            }

            return false;
        }).collect(Collectors.toList()).isEmpty();
    }

    private boolean isValid(int currentCity, int destinationCity, Double[][] mapGraph, Boolean[] visitedCities) {
        if (mapGraph[currentCity][destinationCity] != 0 && !visitedCities[destinationCity]) {
            return true;
        }

        return false;
    }

    private String printSolution(Integer[] citiesPath, Double distanceUntilHere) {
        String result = "Cities path: ";
        for (Integer city : citiesPath) {
            result += city + " ";
        }
        result += "\nTotal distance: " + distanceUntilHere + "\n";

        return result;
    }

    private void backt(int currentCity, int currentPosition, Double[][] mapGraph, List<Location> graphLocations, Integer[] citiesPath, Boolean[] visitedCities, Double distanceUntilHere, List<OrderDetail> leftOrderDetails) {

        List<OrderDetail> orderDetailAuxList = new ArrayList<>(leftOrderDetails);

        for (int city = 0; city < visitedCities.length; city++) {
            if (isValid(currentCity, city, mapGraph, visitedCities)) {
                //TODO update the cities path + visit the current "city"
                citiesPath[city] = currentPosition;
                visitedCities[city] = true;
                //TODO update the distanceUntilHere
                distanceUntilHere += mapGraph[currentCity][city];
                //TODO update order details here but only the parameter
                Location currentLocation = graphLocations.get(city);

                for (OrderDetail orderDetail : leftOrderDetails) {

                    Optional<Integer> quantityContained = strategyCreationService.getQuantityOfProductAtLocation(orderDetail.getProduct(), currentLocation);

                    if (quantityContained.isPresent()) {

                        Integer quantity = quantityContained.get();

                        if (quantity > orderDetail.getQuantity()) {
                            orderDetail.setQuantity(0);
                        } else {
                            orderDetail.setQuantity(orderDetail.getQuantity() - quantity);
                        }
                    }
                }

                if (isSolution(leftOrderDetails)) {
                    System.out.println(printSolution(citiesPath, distanceUntilHere));
                } else {
                    backt(city, currentPosition + 1, mapGraph, graphLocations, citiesPath, visitedCities, distanceUntilHere, leftOrderDetails);
                }

                //TODO RESET UPDATES FROM ABOVE SOLUTION HERE
                citiesPath[city] = 0;
                visitedCities[city] = false;

                distanceUntilHere -= mapGraph[currentCity][city];

                leftOrderDetails = orderDetailAuxList;
            }
        }
    }
}
