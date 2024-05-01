package ru.yandex.practicum.compilation.dto;

import lombok.*;
import ru.yandex.practicum.event.dto.ShortEventDTO;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDTO {
    private Long id;
    private Set<ShortEventDTO> events;
    private Boolean pinned;
    private String title;
}