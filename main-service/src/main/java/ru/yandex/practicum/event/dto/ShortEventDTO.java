package ru.yandex.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.event.location.dto.LocationDTO;
import ru.yandex.practicum.user.dto.ShortUserDTO;

import java.time.LocalDateTime;

import static ru.yandex.practicum.util.Constants.DATE_FORMAT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ShortEventDTO {
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;
    private Long id;
    private ShortUserDTO initiator;
    private LocationDTO location;
    private String title;
    private Long views;
    private Boolean paid;
}