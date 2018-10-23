package ro.msg.learning.shop.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.services.RevenueService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyRevenuesScheduler {

    private final RevenueService revenueService;

    @Scheduled(cron = "0 0 0 * * *")
    public void createDailyRevenues() {
        log.info("Daily revenues scheduler triggered at: " + LocalDateTime.now());
        revenueService.createDailyRevenues();
    }
}
