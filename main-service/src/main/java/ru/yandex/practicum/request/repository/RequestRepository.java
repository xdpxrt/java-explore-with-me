package ru.yandex.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.request.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}