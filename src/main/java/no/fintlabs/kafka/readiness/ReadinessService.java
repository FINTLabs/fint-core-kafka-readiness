package no.fintlabs.kafka.readiness;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.kafka.readiness.config.ReadinessConfig;
import no.fintlabs.kafka.readiness.exception.ReadinessException;
import no.fintlabs.kafka.readiness.offset.OffsetService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadinessService {

    private final OffsetService offsetService;
    private final ReadinessConfig readinessConfig;
    private final RestClient readinessRestClient;

    public boolean isReady() {
        Map<String, Long> newOffsetMap = offsetService.getOffsetMap();
        Map<String, Long> existingOffset = getExistingOffset();
        return newOffsetMapIsEqualsOrHigher(newOffsetMap, existingOffset);
    }

    private boolean newOffsetMapIsEqualsOrHigher(Map<String, Long> newOffsetMap, Map<String, Long> existingOffset) {
        if (existingOffset.size() != newOffsetMap.size()) {
            throw new ReadinessException("The sizes of the offsets are different!");
        }

        for (Map.Entry<String, Long> entry : existingOffset.entrySet()) {
            String key = entry.getKey();
            Long existingValue = entry.getValue();

            if (!newOffsetMap.containsKey(key)) {
                throw new ReadinessException("Key mismatch between offsets: " + key);
            }

            Long offsetValue = newOffsetMap.get(key);

            if (offsetValue < existingValue) {
                log.info("Offset has not caught up yet, awaiting readiness");
                return false;
            }
        }

        log.info("Offset has caught up, switching traffic");
        return true;
    }

    private Map<String, Long> getExistingOffset() {
        return readinessRestClient.get()
                .uri(readinessConfig.getUri())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
