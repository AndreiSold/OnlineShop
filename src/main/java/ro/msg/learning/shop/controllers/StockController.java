package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.services.ExportStocksService;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final ExportStocksService exportStocksService;

    @GetMapping(value = "/export-stocks-from-location/{locationId}", produces = "text/csv")
    public List<StockDto> exportStocks(@PathVariable int locationId) {
        return exportStocksService.getAllStocksByLocationId(locationId);
    }

}
