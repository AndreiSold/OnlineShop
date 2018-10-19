package ro.msg.learning.shop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.distance.DistanceApiResponseDto;

@Service
@Slf4j
public class DistanceCalculatorService {

    @Autowired
    private RestTemplate restTemplate;

    public DistanceApiResponseDto getDistanceApiResultBetweenTwoCities(String originCity, String originCountry, String destinationCity, String destinationCountry) {
        return createDistanceApiResponseEntity(restTemplate, originCity, originCountry, destinationCity, destinationCountry);
    }

    private DistanceApiResponseDto createDistanceApiResponseEntity(RestTemplate restTemplate, String originCity, String originCounty, String destinationCity, String destinationCounty) {

        try {
            ResponseEntity<DistanceApiResponseDto> responseEntity = restTemplate.getForEntity(
                "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + originCity + "," + originCounty + "&destinations=" + destinationCity + "," + destinationCounty + "&key=AIzaSyC19ZDeu_knnKSr15mFt_c1uwTeJHnE0xY", DistanceApiResponseDto.class);
            return responseEntity.getBody();

        } catch (ResourceAccessException e) {
            log.error("Proxy settings are probably wrong! Error at calling the google distance matrix api!");
        }

        return null;
    }

}
