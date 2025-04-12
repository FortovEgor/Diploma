package fortov.egor.diploma.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fortov.egor.diploma.user.dto.NewUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Import({UserMapperImpl.class})
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    User user;

    @BeforeEach
    void setup() {
        user = new User(1L,
                "testusername",
                "testuser@mail.ru",
                "+79779994477",
                "testpassword",
                "testorganization",
                "testavatar_url",
                "test some text about user"
                );
    }

    @Nested
    class CreateUser {
        @Test
        void createUserSuccessfulTest() throws Exception {
            NewUserRequest request = new NewUserRequest(
                    "username", "useremail@mail.ru", "+79565557733", "password");

            User newUser = new User(1L, request.getName(), request.getEmail(), request.getPhone(),
                    "password", null, null, null);

            Mockito
                    .when(userService.createUser(request))
                    .thenReturn(newUser);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", equalTo(newUser.getId()), Long.class))
                    .andExpect(jsonPath("$.name", equalTo(newUser.getName())))
                    .andExpect(jsonPath("$.email", equalTo(newUser.getEmail())))
                    .andExpect(jsonPath("$.phone", equalTo(newUser.getPhone())));
        }

        @Test
        void createUserWithIncorrectEmail_gotException() throws Exception {
            NewUserRequest request = new NewUserRequest(
                    "username", "NOT_A_EMAIL", "+79565557733", "password");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void createUserWithEmptyName_gotException() throws Exception {
            NewUserRequest request = new NewUserRequest(
                    "", "useremail@mail.ru", "+79565557733", "password");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void createUserWithIncorrectPhone_gotException() throws Exception {
            NewUserRequest request = new NewUserRequest(
                    "username", "useremail@mail.ru", "NOT_A_PHONE", "password");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void createUserWithEmptyPassword_gotException() throws Exception {
            NewUserRequest request = new NewUserRequest(
                    "username", "useremail@mail.ru", "+79565557733", "");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class UpdateUser {
        @Test
        void updateUser() throws Exception {
            Mockito
                    .when(userService.updateUser(user))
                    .thenReturn(user);

            mvc.perform(put("/users")
                            .content(mapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful());
        }
    }

    @Nested
    class GettingData {
        @Test
        void getUserByIdSuccessful() throws Exception {
            Mockito
                    .when(userService.getUserById(user.getId()))
                    .thenReturn(user);

            mvc.perform(get("/users/info/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(user.getId()), Long.class))
                    .andExpect(jsonPath("$.name", equalTo(user.getName())))
                    .andExpect(jsonPath("$.email", equalTo(user.getEmail())))
                    .andExpect(jsonPath("$.phone", equalTo(user.getPhone())));
        }

        @Test
        void getUserIdsByName() throws Exception {
            Mockito
                    .when(userService.getUsersByName(user.getName()))
                    .thenReturn(List.of(user));

            String urlTemplate = "/users?name=" + user.getName();
            mvc.perform(get(urlTemplate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0]").value(1L));
        }

        @Test
        void getNotExistingUsersByIds() throws Exception{
            Mockito
                    .when(userService.getNotExistingUserIds(any()))
                    .thenReturn(List.of(2L, 3L));

            String urlTemplate = "/users/notexisting?ids=" + user.getId() + ",2,3";
            mvc.perform(get(urlTemplate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0]").value(2L))
                    .andExpect(jsonPath("$[1]").value(3L));
        }

        @Test
        void getUserIdByEmailAndPassword() throws Exception {
            Mockito
                    .when(userService.getUserIdByEmailAndPassword(user.getEmail(), user.getPassword()))
                    .thenReturn(user.getId());

            String urlTemplate = "/users/userId?email=" + user.getEmail() + "&password=" + user.getPassword();
            mvc.perform(get(urlTemplate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", equalTo(user.getId()), Long.class));
        }

        @Test
        void getUsersByIds() throws Exception {
            Mockito
                    .when(userService.getUsersById(any()))
                    .thenReturn(List.of(user));

            String urlTemplate = "/users/info?ids=" + user.getId() + ",2,3";
            mvc.perform(get(urlTemplate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", equalTo(user.getId()), Long.class))
                    .andExpect(jsonPath("$[0].name", equalTo(user.getName())))
                    .andExpect(jsonPath("$[0].avatar", equalTo(user.getAvatar())));
        }
    }
}