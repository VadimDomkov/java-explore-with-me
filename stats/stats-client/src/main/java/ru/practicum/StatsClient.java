package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {

    public StatsClient(@Value("${stats-server.url}") String urlServer, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(urlServer))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(StatRequestDto dto) {
        return post("/hit", dto);
    }

    public ResponseEntity<Object> getStats(String start, String end, @Nullable List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of("start", encodeTime(start),
                "end", encodeTime(end),
                "unique", unique);

        if (uris == null) {
            params.put("uris", uris);
        }

        return get("/stats?start={start}&end={end}&unique={unique}&uris={uris}", params);
    }

    private String encodeTime(String data) {
        return URLEncoder.encode(data, StandardCharsets.UTF_8);
    }
}