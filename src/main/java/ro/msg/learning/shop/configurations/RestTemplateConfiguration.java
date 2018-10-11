package ro.msg.learning.shop.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@EnableAutoConfiguration
@Slf4j
public class RestTemplateConfiguration {

    @Value("${proxy.host:}")
    private String proxyHost;

    @Value("${proxy.port:0}")
    private int proxyPort;

    @Value("${proxy.used:false}")
    private boolean proxyUsed;

    @Bean
    public RestTemplate restTemplate() {

        if (proxyUsed) {
            SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            clientHttpReq.setProxy(proxy);
            return new RestTemplate(clientHttpReq);
        }

        return new RestTemplate();

    }
}
