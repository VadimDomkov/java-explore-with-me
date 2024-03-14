package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private final WebClient webClient;

    public void addRequest(StatRequestDto requestDto) {
        webClient.post()
                .uri("/hit")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public ResponseEntity<List<StatResponseDto>> getStats(String start, String end, List<String> uris, Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/stats")
                            .queryParam("start", start)
                            .queryParam("end", end)
                            .queryParam("uris", uris)
                            .queryParam("unique", unique)
                            .build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ResponseEntity<List<StatResponseDto>>>() {
                })
                .block();
    }
}