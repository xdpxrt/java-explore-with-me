package ru.yandex.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.yandex.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByInitiatorId(Long userId);
}