package ru.yandex.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.NewUserDTO;
import ru.yandex.practicum.user.dto.UserDTO;
import ru.yandex.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.yandex.practicum.util.Constants.USERS_ADMIN_URI;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(USERS_ADMIN_URI)
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@RequestBody @Valid NewUserDTO newUserDTO) {
        log.info("Response from POST request on {}", USERS_ADMIN_URI);
        return userService.addUser(newUserDTO);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.info("Response from DELETE request on {}{}", USERS_ADMIN_URI, userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDTO> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", USERS_ADMIN_URI);
        return userService.getUsers(ids, fromSizePage(from, size));
    }
}