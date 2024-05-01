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
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat s " +
            "WHERE s.uri IN ?1 AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStatsUrisIn(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.uri LIKE ?1 AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIpUrisIn(String uris, LocalDateTime start, LocalDateTime end);
}
