package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.repositories.ReportRepository;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;

    @GetMapping(value = "/{month}/{year}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getReportForMonthAndYear(@PathVariable int month, @PathVariable int year) {
        return reportRepository.findByMonthEqualsAndYearEquals(month, year).getFile();
    }
}
