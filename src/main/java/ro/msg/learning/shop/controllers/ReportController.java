package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.repositories.ReportRepository;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{month}/{year}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getReportForMonthAndYear(@PathVariable int month, @PathVariable int year) {
        return reportRepository.findByMonthEqualsAndYearEquals(month, year).getFile();
    }
}
