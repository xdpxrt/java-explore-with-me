package ru.yandex.practicum.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.user.dto.NewUserDTO;
import ru.yandex.practicum.user.dto.UserDTO;
import ru.yandex.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(NewUserDTO newUserDTO);

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTO(List<User> users);
}