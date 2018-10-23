package ro.msg.learning.shop.utilities.distance;

import ro.msg.learning.shop.dtos.distance.DistanceResponseDto;

public interface DistanceCalculator {

    DistanceResponseDto getDistanceResponseBetweenTwoCities(String originCity, String originCountry, String destinationCity, String destinationCountry);
}
