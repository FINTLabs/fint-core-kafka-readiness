package no.fintlabs.kafka.readiness.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final ReadinessConfig readinessConfig;

    @Bean
    public RestClient readinessRestClient() {
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(readinessConfig.getPodUrl())
                .build();
    }

    private HttpClient httpClient() {
        HttpClient httpClient = HttpClient.create();
        httpClient.responseTimeout(Duration.ofSeconds(readinessConfig.getTimeoutInSeconds()));
        return httpClient;
    }

}
