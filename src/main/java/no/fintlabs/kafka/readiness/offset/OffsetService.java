package no.fintlabs.kafka.readiness.offset;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
public class OffsetService {

    private final Map<String, Long> offsetMap = new ConcurrentHashMap<>();

    public void updateOffset(String key, Long value) {
        offsetMap.put(key, value);
    }

}
