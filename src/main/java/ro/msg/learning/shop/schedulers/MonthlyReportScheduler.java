package ro.msg.learning.shop.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.ProductDto;
import ro.msg.learning.shop.entities.documents.Report;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.ReportRepository;
import ro.msg.learning.shop.services.ProductService;
import ro.msg.learning.shop.utilities.ExcelFileCreator;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthlyReportScheduler {

    private final ReportRepository reportRepository;
    private final ProductService productService;
    private final ExcelFileCreator excelFileCreator;
    private final ProductMapper productMapper;

    @Scheduled(cron = "0 59 23 30 4,6,9,11 *")
    @Scheduled(cron = "0 59 23 31 1,3,5,7,8,10,12 *")
    @Scheduled(cron = "0 59 23 28 2 *")
    public void createMonthlySoldProductsReport() {

        List<ProductDto> purchasedProducts = productService.getPurchasedProductsInCurrentMonth();

        byte[] fileAsBytes = excelFileCreator.createExcelForProducts(purchasedProducts);

        reportRepository.save(Report.builder()
            .file(fileAsBytes)
            .monthlyProductWrapperList(productMapper.productDtoListToProductWrapper(purchasedProducts))
            .month(LocalDateTime.now().getMonth().getValue())
            .year(LocalDateTime.now().getYear())
            .build());
    }
}
