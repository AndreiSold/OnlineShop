package ro.msg.learning.shop.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationIdDoesNotExistException;
import ro.msg.learning.shop.services.ExportStocksService;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportStocksServiceTest {

    @Autowired
    private ExportStocksService exportStocksService;

    @Test(expected = LocationIdDoesNotExistException.class)
    public void locationIdNotFoundTest() {
        exportStocksService.getAllStocksByLocationId(345215122);
        Assert.assertFalse(true);
    }

    @Test
    public void noStocksFoundInLocationTest() {
        List<Stock> stockListReceived = exportStocksService.getAllStocksByLocationId(3);
        Assert.assertEquals(Collections.emptyList(), stockListReceived);
    }

    @Test
    public void oneStockFoundInLocationTest() {
        List<Stock> stockListReceived = exportStocksService.getAllStocksByLocationId(2);
        Assert.assertEquals(1, stockListReceived.size());
    }

    @Test
    public void moreThanOneStocksFoundInLocationTest() {
        List<Stock> stockListReceived = exportStocksService.getAllStocksByLocationId(8);
        Assert.assertTrue(stockListReceived.size() > 1);
    }

}