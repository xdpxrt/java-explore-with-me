package ru.yandex.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.event.location.dto.LocationDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.yandex.practicum.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDTO {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private int category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @Future
    @NotNull
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDTO locationDTO;
    @NotNull
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}