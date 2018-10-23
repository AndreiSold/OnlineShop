package ro.msg.learning.shop.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.ResultedStockListEmptyException;
import ro.msg.learning.shop.services.StockService;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Test(expected = LocationNotFoundException.class)
    public void locationIdNotFoundTest() {
        stockService.getAllStocksByLocationId(-1);
    }

    @Test(expected = ResultedStockListEmptyException.class)
    public void noStocksFoundInLocationTest() {
        List<StockDto> stockListReceived = stockService.getAllStocksByLocationId(999);
        Assert.assertEquals(Collections.emptyList(), stockListReceived);
    }

    @Test
    public void oneStockFoundInLocationTest() {
        List<StockDto> stockListReceived = stockService.getAllStocksByLocationId(9);
        Assert.assertEquals(1, stockListReceived.size());
    }

    @Test
    public void moreThanOneStockFoundInLocationTest() {
        List<StockDto> stockListReceived = stockService.getAllStocksByLocationId(8);
        Assert.assertTrue(stockListReceived.size() > 1);
    }

}