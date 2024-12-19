package no.fintlabs.kafka.readiness;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.kafka.readiness.exception.ReadinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@RestController
@RequestMapping("/ready")
@RequiredArgsConstructor
public class ReadinessController {

    private final ReadinessService readinessService;

    @GetMapping
    public ResponseEntity<Void> isReady() {
        try {
            return readinessService.isReady()
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (ReadinessException e) {
            log.error("ReadinessException during check: {}", e.getMessage());
            return ResponseEntity.ok().build();
        } catch (RestClientResponseException e) {
            log.error("Request failed with status code: {}", e.getStatusCode());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Unexpected error has occured: {}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }

}
