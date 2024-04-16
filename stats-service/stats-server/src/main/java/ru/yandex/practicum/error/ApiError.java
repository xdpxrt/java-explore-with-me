package ru.yandex.practicum.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}