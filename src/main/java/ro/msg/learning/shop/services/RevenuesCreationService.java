package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.RevenueRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RevenuesCreationService {

    private final LocationRepository locationRepository;
    private final RevenueRepository revenueRepository;

    public void createDailyRevenues() {
        List<Object[]> revenuesAsObjects = locationRepository.getDailyRevenuesAsObjects();

        revenuesAsObjects.parallelStream().forEach(revenueObject -> {
                Revenue newRevenue = Revenue.builder().location((Location) revenueObject[0])
                    .sum((Double) revenueObject[1])
                    .timestamp((LocalDateTime) revenueObject[2])
                    .build();

                revenueRepository.save(newRevenue);
            }
        );
    }


}
