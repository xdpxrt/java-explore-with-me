package ru.yandex.practicum.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}