package ru.yandex.practicum.request.model;

import lombok.*;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.util.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Constants.RequestStatus status;
}