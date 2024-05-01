package ru.yandex.practicum.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.yandex.practicum.event.model.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    List<Event> findAllByIdIn(Set<Long> ids);
}