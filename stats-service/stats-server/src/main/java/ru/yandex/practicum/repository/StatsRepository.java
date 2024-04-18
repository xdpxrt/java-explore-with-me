package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dto.ViewStats;
import ru.yandex.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStatsUrisIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIpUrisIn(LocalDateTime start, LocalDateTime end, List<String> uris);

}