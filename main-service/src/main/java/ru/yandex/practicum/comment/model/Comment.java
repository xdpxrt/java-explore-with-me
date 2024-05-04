package ru.yandex.practicum.comment.model;

import lombok.*;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 512)
    private String text;
    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;
    @Column(nullable = false, name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}