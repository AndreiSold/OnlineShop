package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.RevenueRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RevenueService {

    private final LocationRepository locationRepository;
    private final RevenueRepository revenueRepository;

    public List<Revenue> createDailyRevenues() {
        List<Revenue> revenuesCreated = locationRepository.createDailyRevenues();

        revenuesCreated.parallelStream().forEach(revenueRepository::save);

        return revenuesCreated;
    }

}
