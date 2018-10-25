package ro.msg.learning.shop.utilities.distance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.distance.DistanceResponseDto;

@Slf4j
@RequiredArgsConstructor
public class GoogleDistanceCalculator implements DistanceCalculator {

    private final RestTemplate restTemplate;

    @Value("${google-api-key}")
    private String key;

    public DistanceResponseDto getDistanceResponseBetweenTwoCities(String originCity, String originCountry, String destinationCity, String destinationCountry) {
        return createDistanceResponseEntity(restTemplate, originCity, originCountry, destinationCity, destinationCountry);
    }

    private DistanceResponseDto createDistanceResponseEntity(RestTemplate restTemplate, String originCity, String originCounty, String destinationCity, String destinationCounty) {

        try {
            ResponseEntity<DistanceResponseDto> responseEntity = restTemplate.getForEntity(
                "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + originCity + "," + originCounty + "&destinations=" + destinationCity + "," + destinationCounty + "&key=" + key, DistanceResponseDto.class);
            return responseEntity.getBody();

        } catch (ResourceAccessException e) {
            log.error("Proxy settings are probably wrong! Error at calling the google distance matrix api!");
        }

        return null;
    }

}
