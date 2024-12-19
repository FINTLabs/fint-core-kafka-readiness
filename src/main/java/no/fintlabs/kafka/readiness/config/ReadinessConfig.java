package no.fintlabs.kafka.readiness.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties("fint.readiness")
public class ReadinessConfig {

    private String podUrl;
    private String uri = "";

}
