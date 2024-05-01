package ru.yandex.practicum.event.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    @NotNull
    private float lat;
    @NotNull
    private float lon;
}