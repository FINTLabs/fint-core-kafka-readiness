package no.fintlabs.kafka.readiness.exception;

public class ReadinessException extends RuntimeException {
    public ReadinessException(String message) {
        super(message);
    }
}
