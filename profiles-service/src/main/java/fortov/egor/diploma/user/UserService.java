package fortov.egor.diploma.user;

import fortov.egor.diploma.exception.ConflictException;
import fortov.egor.diploma.exception.DBException;
import fortov.egor.diploma.exception.NotFoundException;
import fortov.egor.diploma.exception.NotValidInputParamException;
import fortov.egor.diploma.user.dto.NewUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

        User user;
        try {
            user = repo.save(mapper.toUser(request));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("user with such email or phone already exists");
        } catch (Exception e) {
            throw new DBException();
        }

        return user;
    }

    @Transactional
    public User updateUser(User user) {
        log.info("updating user with id = {}", user.getId());
        if (user.getId() == null || user.getId() < 0) {
            throw new NotValidInputParamException("id должен быть > ноля");
        }

        User currentUserData;
        try {
            currentUserData = repo.findUserById(user.getId());
        } catch (Exception e) {
            throw new DBException();
        }

        if (currentUserData == null) {
            throw new NotFoundException("No user with such id");
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

        try {
            user = repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("user with such email or phone already exists");
        } catch (Exception e) {
            throw new DBException();
        }

        return user;
    }

    public User getUserById(Long id) {
        log.info("getting user with id = {}", id);
        if (id <= 0) {
            throw new NotValidInputParamException("id должен быть > ноля");
        }

        User possibleUser;
        try {
            possibleUser = repo.findUserById(id);
        } catch (Exception e) {
            throw new DBException();
        }

        if (possibleUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        log.info("user {} got from DB", possibleUser);
        return possibleUser;
    }

    public List<User> getUsersByName(String name) {
        log.info("getting users by name = {}", name);

        List<User> users;
        try {
            users = repo.findUsersByName(name);
        } catch (Exception e) {
            throw new DBException();
        }

        if (users == null || users.isEmpty()) {
            throw new NotFoundException("Failed to find any users with name " + name);
        }

        return users;
    }

    public List<Long> getNotExistingUserIds(List<Long> ids) {
        log.info("getting not existing user ids from ids = {}", ids);
        List<Long> existingIds;
        try {
            existingIds = repo.findExistingUserIds(ids);
        } catch (Exception e) {
            throw new DBException();
        }

        return ids.stream()
                .filter((id) -> !existingIds.contains(id))
                .toList();
    }

    public Long getUserIdByEmailAndPassword(String email, String password) {
        log.info("getting user ids by email = {} & password = {}", email, password);
        Long id;
        try {
            id = repo.getIdByEmailAndPassword(email, password);
        } catch (Exception e) {
            throw new DBException();
        }

        if (id == null) {
            throw new NotFoundException("Failed to find user with email = " + email + " and password = " + password);
        }
        return id;
    }

    public List<User> getUsersById(List<Long> ids) {
        log.info("getting users by ids = {}", ids);
        List<User> users;
        try {
            users = repo.getUsersById(ids);
        } catch (Exception e) {
            throw new DBException();
        }

        if (users == null || users.isEmpty()) {
            throw new NotFoundException("Failed to find any users with ids = " + ids);
        }
        return users;
    }

    public List<User> getAllUsers() {
        log.info("getting all users...");
        List<User> users;
        try {
            users = repo.getAllUsers();
        } catch (Exception e) {
            throw new DBException();
        }

        if (users == null || users.isEmpty()) {
            throw new NotFoundException("Failed to find any users");
        }
        return users;
    }
}
