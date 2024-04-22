package ru.yandex.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.event.location.model.Location;
import ru.yandex.practicum.event.state.EventState;
import ru.yandex.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @JoinColumn(name = "location_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private int participantLimit;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;

    @JoinColumn(name = "initiator_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;

    @Column(name = "event_state")
    private EventState state;
}