package ru.yandex.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.event.location.dto.LocationDTO;
import ru.yandex.practicum.user.dto.UserShortDTO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortEventDTO {
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDTO initiator;
    private LocationDTO location;
    private String title;
    private Long views;
}