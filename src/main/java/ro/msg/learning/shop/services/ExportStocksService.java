package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationIdDoesNotExistException;
import ro.msg.learning.shop.repositories.LocationRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportStocksService {

    private final LocationRepository locationRepository;

    public List<Stock> getAllStocksByLocationId(int locationId) {
        Optional<Location> givenOptionalLocation = locationRepository.findById(locationId);

        if (givenOptionalLocation.isPresent()) {
            Location givenLocation = givenOptionalLocation.get();
            return locationRepository.getAllStocksByLocation(givenLocation);
        }

        log.error("Given location id does not exist. This is the given value: " + locationId);
        throw new LocationIdDoesNotExistException(locationId);
    }
}
