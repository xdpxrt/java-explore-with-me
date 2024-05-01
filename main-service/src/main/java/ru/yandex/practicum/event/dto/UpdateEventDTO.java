package ru.yandex.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.event.location.dto.LocationDTO;
import ru.yandex.practicum.util.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.yandex.practicum.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventDTO {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @Future
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime eventDate;
    @Valid
    private LocationDTO location;
    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;
    private Constants.EventStatus stateAction;
}