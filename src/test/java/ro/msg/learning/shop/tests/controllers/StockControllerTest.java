package ro.msg.learning.shop.tests.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.utilities.CsvConverter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class StockControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CsvConverter csvConverter;

    private String basePath;
    private OAuth2RestTemplate oAuth2RestTemplate;


    @Before
    public void init() {
        basePath = "http://localhost:" + port + "/stock";

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setPassword("1234");
        resourceDetails.setUsername("admin");
        resourceDetails.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setScope(asList("read", "write"));
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
    }

    @Test(expected = HttpClientErrorException.class)
    public void exportStocksNullResultTest() {
        final int locationId = 999;

        oAuth2RestTemplate.getForEntity(basePath + "/export-stocks-from-location/" + locationId, String.class);
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void exportStocksOneItemResultTest() {
        String exportStocksUrl = basePath + "/export-stocks-from-location";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.valueOf("text/csv")));

        HttpEntity<String> entity = new HttpEntity<>("headers", httpHeaders);

        ResponseEntity<String> response = oAuth2RestTemplate.exchange(exportStocksUrl + "/5", HttpMethod.GET, entity, String.class);

        String responseString = response.getBody();
        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        List<Stock> stockList = csvConverter.fromCsv(Stock.class, inputStream);
        Stock singleObject = stockList.get(0);

        Assert.assertEquals(1, stockList.size());
        Assert.assertEquals(44, singleObject.getQuantity().intValue());
        Assert.assertEquals(292, singleObject.getId().intValue());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void exportStocksThreeItemResultTest() {
        String exportStocksUrl = basePath + "/export-stocks-from-location";
        ResponseEntity<String> response = oAuth2RestTemplate.getForEntity(exportStocksUrl + "/8", String.class);

        String responseString = response.getBody();
        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        List<Stock> stockList = csvConverter.fromCsv(Stock.class, inputStream);

        Assert.assertEquals(3, stockList.size());

        Stock firstObject = stockList.get(0);
        Stock secondObject = stockList.get(1);
        Stock thirdObject = stockList.get(2);

        Assert.assertEquals(436, firstObject.getQuantity().intValue());
        Assert.assertEquals(232, firstObject.getId().intValue());

        Assert.assertEquals(438, secondObject.getQuantity().intValue());
        Assert.assertEquals(462, secondObject.getId().intValue());

        Assert.assertEquals(245, thirdObject.getQuantity().intValue());
        Assert.assertEquals(812, thirdObject.getId().intValue());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
