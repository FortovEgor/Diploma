package fortov.egor.diploma.user;

import fortov.egor.diploma.exception.NotFoundException;
import fortov.egor.diploma.exception.NotValidIdException;
import fortov.egor.diploma.user.dto.NewUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.info("creating user: {}", request);
        User user = mapper.toUser(request);
        return repo.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        if (user.getId() == null || user.getId() < 0) {
            throw new NotValidIdException();
        }
        log.info("updating user with id = {}");
        User currentUserData = repo.findUserById(user.getId());
        if (currentUserData == null) {
            throw new NotFoundException("No user with such id!");
        }
        if (user.getName() == null) {
            user.setName(currentUserData.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(currentUserData.getEmail());
        }
        if (user.getPhone() == null) {
            user.setPhone(currentUserData.getPhone());
        }
        if (user.getPassword() == null) {
            user.setPassword(currentUserData.getPassword());
        }
        if (user.getOrganization() == null) {
            user.setOrganization(currentUserData.getOrganization());
        }
        if (user.getAvatar() == null) {
            user.setAvatar(currentUserData.getAvatar());
        }
        if (user.getAbout() == null) {
            user.setAbout(currentUserData.getAbout());
        }
        return repo.save(user);
    }

    public User getUserById(Long id) {
        log.info("getting user with id = {}", id);
        if (id <= 0) {
            throw new NotValidIdException();
        }

        User possibleUser = repo.findUserById(id);
        log.info("user {} got from DB", possibleUser);
        if (possibleUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return possibleUser;
    }

    public List<User> getUsersByName(String name) {
        log.info("getting users by name = {}", name);
        return repo.findUsersByName(name);
    }

    public List<Long> getNotExistingUserIds(List<Long> ids) {
        log.info("getting not existing user ids from ids = {}", ids);
        List<Long> existingIds = repo.findExistingUserIds(ids);
        return ids.stream()
                .filter((id) -> !existingIds.contains(id))
                .toList();
    }

    public List<Long> getUserIdsByUsernameAndPassword(String username, String password) {
        log.info("getting user ids by username = {} & password = {}", username, password);
        return repo.getIdsByUsernameAndPassword(username, password);
    }

    public List<User> getUsersById(List<Long> ids) {
        log.info("getting users by ids = {}", ids);
        return repo.getUsersById(ids);
    }
}
