package ro.msg.learning.shop.tests.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.utilities.CsvConverter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CsvConverter csvConverter;

    private String basePath;
    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Before
    public void init() {
        basePath = "http://localhost:" + port + "/stocks";
    }

    @Test
    public void exportStocksNullResultTest() {
        final int locationId = 999;

        ResponseEntity<String> responseEntity = testRestTemplate.withBasicAuth("admin", "1234").getForEntity(basePath + "/export-stocks-from-location/" + locationId, String.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
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

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "1234")
            .exchange(exportStocksUrl + "/5", HttpMethod.GET, entity, String.class);

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
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "1234").getForEntity(exportStocksUrl + "/8", String.class);

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
