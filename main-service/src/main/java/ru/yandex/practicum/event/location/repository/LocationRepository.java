package ru.yandex.practicum.event.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.event.location.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}