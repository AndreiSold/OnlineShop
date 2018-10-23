package ro.msg.learning.shop.mappers;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.entities.Stock;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockDtoMapper {

    public List<StockDto> stockToStockDto(List<Stock> stockList) {

        List<StockDto> stockDtoList = new ArrayList<>();

        stockList.forEach(stock -> {
            stockDtoList.add(new StockDto(stock.getId(), stock.getQuantity()));
        });

        return stockDtoList;
    }
}
