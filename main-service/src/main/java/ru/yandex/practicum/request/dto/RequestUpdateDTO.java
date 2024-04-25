package ru.yandex.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.request.status.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDTO {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus requestStatus;
}