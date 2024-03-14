package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatResponseDto;
import ru.practicum.model.StatHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatHit, Long> {
    @Query("SELECT NEW ru.practicum.StatResponseDto(" +
            "hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM StatHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(hits) DESC")
    List<StatResponseDto> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.StatResponseDto(" +
            "hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM StatHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(DISTINCT hits) DESC")
    List<StatResponseDto> getAllUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.StatResponseDto(" +
            "hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM StatHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "AND hits.uri IN ?3 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(hits) DESC")
    List<StatResponseDto> getStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT NEW ru.practicum.StatResponseDto(" +
            "hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM StatHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "AND hits.uri IN ?3 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(DISTINCT hits) DESC")
    List<StatResponseDto> getUniqueStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

}
