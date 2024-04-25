package ru.yandex.practicum.event.dto;

import lombok.*;
import ru.yandex.practicum.event.state.StateAction;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateEventDTO extends BaseUpdateEventDTO {
    private StateAction stateAction;
}