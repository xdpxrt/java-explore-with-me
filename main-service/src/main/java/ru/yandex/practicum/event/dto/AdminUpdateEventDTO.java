package ru.yandex.practicum.event.dto;

import lombok.*;
import ru.yandex.practicum.event.state.AdminStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventDTO extends BaseUpdateEventDTO {
    private AdminStatus stateAction;
}