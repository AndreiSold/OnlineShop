package ro.msg.learning.shop.tests.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.distance.DistanceApiResponseDto;
import ro.msg.learning.shop.exceptions.ProxyBadConfiguredException;
import ro.msg.learning.shop.services.DistanceCalculatorService;

import java.net.InetSocketAddress;
import java.net.Proxy;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles(profiles = "dev")
public class DistanceCalculatorServiceTest {

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private String proxyPort;

    @Value("${proxy.used}")
    private String proxyUsed;

    @Autowired
    private DistanceCalculatorService distanceCalculatorService;

    @Test
    public void theirExampleTest() {

        if ("true".equals(proxyUsed)) {

            SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
            clientHttpReq.setProxy(proxy);

            RestTemplate restTemplate = new RestTemplate(clientHttpReq);

            createResponseEntity(restTemplate);

        } else if ("false".equals(proxyUsed)) {

            RestTemplate restTemplate = new RestTemplate();

            createResponseEntity(restTemplate);
        } else {
            log.error("Proxy used should be true or false! The current value is: " + proxyUsed);
            throw new ProxyBadConfiguredException("Proxy used should be true or false! The current value is: " + proxyUsed);
        }
    }

    @Test
    public void serviceTest() {
        log.info(distanceCalculatorService.getDistanceApiResultBetweenTwoCities("Washington", "DC", "New York City", "NY").toString());
        Assert.assertTrue(true);
    }

    @Test
    public void serviceMyTest() {
        log.info(distanceCalculatorService.getDistanceApiResultBetweenTwoCities("Arad", "Arad", "New York City", "NY").toString());
        Assert.assertTrue(true);
    }

    private final void createResponseEntity(RestTemplate restTemplate) {
        try {
            ResponseEntity<DistanceApiResponseDto> responseEntity = restTemplate.getForEntity(
                "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyC19ZDeu_knnKSr15mFt_c1uwTeJHnE0xY", DistanceApiResponseDto.class);
            log.info(responseEntity.getBody().toString());
            Assert.assertTrue(true);
        } catch (ResourceAccessException e) {
            log.error("Proxy settings are probably wrong! The current settings are:\nProxy host: " + proxyHost + "\nProxy port: " + proxyPort + "\nProxy used: " + proxyUsed);
            throw new ProxyBadConfiguredException("Proxy settings are probably wrong! The current settings are:\nProxy host: " + proxyHost + "\nProxy port: " + proxyPort + "\nProxy used: " + proxyUsed);
        }
    }
}
