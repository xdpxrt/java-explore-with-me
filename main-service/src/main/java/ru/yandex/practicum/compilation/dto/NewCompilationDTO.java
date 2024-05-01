package ru.yandex.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDTO {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    @Size(max = 50)
    private String title;
}