package fortov.egor.diploma.user;

import fortov.egor.diploma.user.dto.NewUserRequest;
import fortov.egor.diploma.user.dto.UserDto;
import fortov.egor.diploma.user.dto.UserDtoPartial;
import fortov.egor.diploma.user.dto.UserFullInfoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public UserDto createUser(@Valid @RequestBody NewUserRequest request) {
        User user = userService.createUser(request);
        return userMapper.toDto(user);
    }

    @PutMapping
    public Long put(@RequestBody User user) {
        return userService.updateUser(user).getId();
    }

    @GetMapping
    public UserFullInfoDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toFullInfo(user);
    }

    @GetMapping
    public List<Long> getUserIdsByName(@PathVariable String name) {
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

    @GetMapping("/usersIds")
    public List<Long> getUserIdsByUsernameAndPassword(@RequestParam String name,
                                                      @RequestParam String password) {
        return userService.getUserIdsByUsernameAndPassword(name, password);
    }

    @GetMapping("/info")
    public List<UserDtoPartial> getUsersByIds(@RequestParam List<Long> ids) {
        return userMapper.toPartial(userService.getUsersById(ids));
    }
}
