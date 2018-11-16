package ro.msg.learning.shop.tests.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.documents.Report;
import ro.msg.learning.shop.repositories.ReportRepository;

import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportControllerTest {

    private static final String FILE_AS_STRING = "12";

    @Autowired
    private ReportRepository reportRepository;

    @LocalServerPort
    private int port;

    private String basePath;

    private OAuth2RestTemplate oAuth2RestTemplate;

    @Before
    public void init() {
        basePath = "http://localhost:" + port + "/report";

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setPassword("1234");
        resourceDetails.setUsername("admin");
        resourceDetails.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setScope(asList("read", "write"));
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
    }

    @Test
    public void testReportRetrievalAfterMonthAndYear() {

        int yearIn100Years = LocalDateTime.now().getYear() + 100;
        int month = 12;

        final Report createdReport = reportRepository.save(Report.builder()
            .file(FILE_AS_STRING.getBytes())
            .month(month)
            .year(yearIn100Years)
            .build());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE)));

        HttpEntity<String> request = new HttpEntity<>("headers", httpHeaders);

        ResponseEntity<byte[]> responseEntity = oAuth2RestTemplate
            .exchange(basePath + "/" + month + "/" + yearIn100Years, HttpMethod.GET, request, byte[].class);

        byte[] response = responseEntity.getBody();

        reportRepository.deleteById(createdReport.getId());

        Assert.assertEquals(FILE_AS_STRING, new String(response));
    }
}
