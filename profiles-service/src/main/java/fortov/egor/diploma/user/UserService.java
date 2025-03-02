package fortov.egor.diploma.user;

import fortov.egor.diploma.exception.NotFoundException;
import fortov.egor.diploma.exception.NotValidIdException;
import fortov.egor.diploma.user.dto.NewUserRequest;
import fortov.egor.diploma.user.dto.UserDtoPartial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Transactional
    public User createUser(NewUserRequest request) {
        User user = mapper.toUser(request);
        // @TODO: add logging using filter EVERYWHERE
        return repo.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        if (repo.findUserById(user.getId()) == null) {
            throw new NotFoundException("No film with such id!");
        }
        return repo.update(user);
    }

    public User getUserById(Long id) {
        if (id <= 0) {
            throw new NotValidIdException();
        }

        User possibleUser = repo.findUserById(id);
        if (possibleUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Получен фильм с айди {}.", id);
        return possibleUser;
    }

    public List<User> getUsersByName(String name) {
        return repo.findUsersByName(name);
    }

    public List<Long> getNotExistingUserIds(List<Long> ids) {
        List<Long> existingIds = repo.findExistingUserIds(ids);
        return ids.stream()
                .filter((id) -> !existingIds.contains(id))
                .toList();
    }

    public List<Long> getUserIdsByUsernameAndPassword(String username, String password) {
        return repo.getIdsByUsernameAndPassword(username, password);
    }

    public List<User> getUsersById(List<Long> ids) {
        return repo.getUsersById(ids);
    }
}
