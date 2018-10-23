package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.enums.DistanceApiEnum;
import ro.msg.learning.shop.exceptions.DistanceApiNonexistentException;
import ro.msg.learning.shop.utilities.distance.DistanceCalculator;
import ro.msg.learning.shop.utilities.distance.GoogleDistanceCalculator;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DistanceCalculatorConfiguration {

    private final RestTemplate restTemplate;

    @Value("${distance-api}")
    private String chosenDistanceApi;

    @Bean
    public DistanceCalculator distanceCalculator() {
        switch (DistanceApiEnum.valueOf(chosenDistanceApi)) {
            case GOOGLE_DISTANCE_MATRIX:
                return new GoogleDistanceCalculator(restTemplate);
            default:
                log.error("Given distance api is not one of our options! Given api name: " + chosenDistanceApi);

                StringBuilder existingApis = new StringBuilder();

                for (DistanceApiEnum distanceApi : DistanceApiEnum.values()) {
                    existingApis.append(distanceApi.name()).append(" ");
                }

                throw new DistanceApiNonexistentException(chosenDistanceApi, "Existent distance APIs: " + existingApis);
        }
    }
}
