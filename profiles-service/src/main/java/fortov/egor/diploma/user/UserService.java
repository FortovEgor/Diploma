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
        List<User> users = repo.findUsersByName(name);
        if (users == null) {
            throw new NotFoundException("Failed to find any users with name " + name);
        }
        return users;
    }

    public List<Long> getNotExistingUserIds(List<Long> ids) {
        log.info("getting not existing user ids from ids = {}", ids);
        List<Long> existingIds = repo.findExistingUserIds(ids);
        if (existingIds == null) {
            throw new NotFoundException("Failed to find any users with id from " + ids);
        }
        return ids.stream()
                .filter((id) -> !existingIds.contains(id))
                .toList();
    }

    public Long getUserIdByEmailAndPassword(String email, String password) {
        log.info("getting user ids by email = {} & password = {}", email, password);
        Long id =  repo.getIdByEmailAndPassword(email, password);
        if (id == null) {
            throw new NotFoundException("Failed to find user with email = " + email + " and password = " + password);
        }
        return id;
    }

    public List<User> getUsersById(List<Long> ids) {
        log.info("getting users by ids = {}", ids);
        List<User> users = repo.getUsersById(ids);
        if (users == null) {
            throw new NotFoundException("Failed to find any users with ids = " + ids);
        }
        return users;
    }

    public List<User> getAllUsers() {
        log.info("getting all users...");
        List<User> users = repo.getAllUsers();
        if (users == null) {
            throw new NotFoundException("Failed to find any users");
        }
        return users;
    }
}
