package ru.yandex.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationDTO {
    private Set<Long> events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}