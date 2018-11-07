package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.services.StockService;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/export-stocks-from-location/{locationId}", produces = "text/csv")
    public List<StockDto> exportStocksFromLocation(@PathVariable int locationId) {
        return stockService.getAllStocksByLocationId(locationId);
    }

}
