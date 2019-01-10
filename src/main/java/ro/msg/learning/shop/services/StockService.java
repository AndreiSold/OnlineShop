package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.StockDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.ResultedStockListEmptyException;
import ro.msg.learning.shop.mappers.StockDtoMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.repositories.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final StockDtoMapper stockDtoMapper;
    private final ProductRepository productRepository;

    public List<StockDto> getAllStocksByLocationId(int locationId) {
        Optional<Location> givenOptionalLocation = locationRepository.findById(locationId);

        if (givenOptionalLocation.isPresent()) {
            Location givenLocation = givenOptionalLocation.get();
            List<Stock> stockResultList = stockRepository.getAllStocksByLocation(givenLocation);

            if (stockResultList.isEmpty()) {
                log.error("No stocks have been found in location: ", locationId);
                throw new ResultedStockListEmptyException(locationId, "No stocks have been found!");
            }

            return stockDtoMapper.stockToStockDto(stockResultList);
        }

        log.error("Given location id does not exist. This is the given value: ", locationId);
        throw new LocationNotFoundException(locationId, "Try another location id!");
    }

    public void addStock(String productName, int quantity) {

        Optional<Product> product = productRepository.findByName(productName);

        if (product.isPresent()) {
            Stock createdStock = new Stock();
            createdStock.setProduct(product.get());
            createdStock.setQuantity(quantity);
            createdStock.setLocation(locationRepository.findById(1).get());
            stockRepository.save(createdStock);
        }
    }
}
