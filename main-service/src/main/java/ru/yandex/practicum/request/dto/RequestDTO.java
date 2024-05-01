package ru.yandex.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.util.Constants;

import java.time.LocalDateTime;

import static ru.yandex.practicum.util.Constants.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private Constants.RequestStatus status;
}