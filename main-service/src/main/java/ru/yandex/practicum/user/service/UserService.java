package ru.yandex.practicum.user.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO addUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);

    List<UserDTO> getUsers(List<Long> ids, PageRequest pageRequest);
}