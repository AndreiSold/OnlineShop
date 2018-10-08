package ro.msg.learning.shop.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.ResultedStockListEmptyException;
import ro.msg.learning.shop.services.ExportStocksService;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportStocksServiceTest {

    @Autowired
    private ExportStocksService exportStocksService;

    @Test(expected = LocationNotFoundException.class)
    public void locationIdNotFoundTest() {
        exportStocksService.getAllStocksByLocationId(-1);
    }

    @Test(expected = ResultedStockListEmptyException.class)
    public void noStocksFoundInLocationTest() {
        List<StockDto> stockListReceived = exportStocksService.getAllStocksByLocationId(3);
        Assert.assertEquals(Collections.emptyList(), stockListReceived);
    }

    @Test
    public void oneStockFoundInLocationTest() {
        List<StockDto> stockListReceived = exportStocksService.getAllStocksByLocationId(2);
        Assert.assertEquals(1, stockListReceived.size());
    }

    @Test
    public void moreThanOneStockFoundInLocationTest() {
        List<StockDto> stockListReceived = exportStocksService.getAllStocksByLocationId(8);
        Assert.assertTrue(stockListReceived.size() > 1);
    }

}