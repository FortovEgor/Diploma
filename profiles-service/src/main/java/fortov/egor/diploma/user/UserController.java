package fortov.egor.diploma.user;

import fortov.egor.diploma.user.dto.NewUserRequest;
import fortov.egor.diploma.user.dto.UserDto;
import fortov.egor.diploma.user.dto.UserDtoPartial;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest request) {
        User user = userService.createUser(request);
        return userMapper.toDto(user);
    }

    @PutMapping
    public Long put(@RequestBody User user) {
        return userService.updateUser(user).getId();
    }

    @GetMapping("/info/{id}")
    public UserFullInfoDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toFullInfo(user);
    }

    @GetMapping
    public List<Long> getUserIdsByName(@RequestParam(name = "name") String name) {
        List<User> users = userService.getUsersByName(name);
        List<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return userIds;
    }

    @GetMapping("/notexisting")
    public List<Long> getNotExistingUsersByIds(@RequestParam List<Long> ids) {
        return userService.getNotExistingUserIds(ids);
    }

    @GetMapping("/userId")
    public Long getUserIdByEmailAndPassword(@RequestParam String email,
                                            @RequestParam String password) {
        return userService.getUserIdByEmailAndPassword(email, password);
    }

    @GetMapping("/info")
    public List<UserDtoPartial> getUsersByIds(@RequestParam List<Long> ids) {
        return userMapper.toPartial(userService.getUsersById(ids));
    }

    @GetMapping("/all")
    public List<UserFullInfoDto> getAllUsers() {
        return userMapper.toFullInfo(userService.getAllUsers());
    }
}
