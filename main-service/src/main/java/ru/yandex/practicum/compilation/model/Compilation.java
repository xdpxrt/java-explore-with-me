package ru.yandex.practicum.compilation.model;

import lombok.*;
import ru.yandex.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
}