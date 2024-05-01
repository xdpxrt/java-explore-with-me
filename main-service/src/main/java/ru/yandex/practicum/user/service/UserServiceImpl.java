package ru.yandex.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.user.dto.NewUserDTO;
import ru.yandex.practicum.user.dto.UserDTO;
import ru.yandex.practicum.user.mapper.UserMapper;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.util.List;

import static ru.yandex.practicum.util.Constants.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO addUser(NewUserDTO newUserDTO) {
        log.debug("Adding user {}", newUserDTO);
        User user = userRepository.save(userMapper.toUser(newUserDTO));
        log.debug("User is added {}", user);
        return userMapper.toUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("Deleting user ID{}", userId);
        if (!userRepository.existsById(userId)) throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        userRepository.deleteById(userId);
        log.debug("User ID{} is deleted", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(List<Long> ids, PageRequest pageRequest) {
        log.debug("Getting list of users");
        return (ids.isEmpty()) ? userMapper.toUserDTO(userRepository.findAll(pageRequest).toList())
                : userMapper.toUserDTO(userRepository.findAllByIdIn(ids, pageRequest));
    }
}