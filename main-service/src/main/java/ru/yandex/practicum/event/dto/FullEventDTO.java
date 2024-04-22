package ru.yandex.practicum.event.dto;

import lombok.*;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.event.location.dto.LocationDTO;
import ru.yandex.practicum.event.state.EventState;
import ru.yandex.practicum.user.dto.UserShortDTO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullEventDTO {
    private String annotation;
    private CategoryDTO category;
    private int confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDTO initiator;
    private LocationDTO location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}