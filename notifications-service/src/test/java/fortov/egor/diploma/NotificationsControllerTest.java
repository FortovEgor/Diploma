package fortov.egor.diploma;



import com.fasterxml.jackson.databind.ObjectMapper;
import fortov.egor.diploma.dto.CreateNotificationRequest;
import fortov.egor.diploma.dto.UpdateNotificationRequest;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Import({NotificationsMapperImpl.class})
@WebMvcTest(controllers = NotificationsController.class)
class NotificationsControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NotificationsService notificationsService;

    Notification notification;

    @BeforeEach
    void setup() {
        // creating valid object
        notification = new Notification(
                1L,
                "sms",
                "This is a notification content",
                LocalDateTime.now(),
                Duration.ofMinutes(5),
                1L,
                false
        );
    }

    @Nested
    class CreateNotification {
        @Test
        void createNotificationSuccessful() throws Exception {
            CreateNotificationRequest request = new CreateNotificationRequest(
                    "sms",
                    "This is a notification content",
                    LocalDateTime.now().toString(),
                    Duration.ofMinutes(5),
                    1L,
                    false);

            Mockito
                    .when(notificationsService.create(request))
                    .thenReturn(notification);

            mvc.perform(post("/notifications")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", equalTo(notification.getId()), Long.class))
                    .andExpect(jsonPath("$.type", equalTo(notification.getType())))
                    .andExpect(jsonPath("$.content", equalTo(notification.getContent())))
                    .andExpect(jsonPath("$.time_to_show", equalTo(notification.getTime_to_show().toString())))
                    .andExpect(jsonPath("$.interval_to_repeat", equalTo(notification.getInterval_to_repeat().toString())))
                    .andExpect(jsonPath("$.userId", equalTo(notification.getUserId()), Long.class))
                    .andExpect(jsonPath("$.immediately", equalTo(notification.getImmediately())));
        }

        // @TODO: add more tests
    }

    @Nested
    class UpdateNotification {
        @Test
        void updateNotificationSuccessful() throws Exception {
            UpdateNotificationRequest request = new UpdateNotificationRequest(
                    1L,
                    "sms",
                    "This is a notification content",
                    LocalDateTime.now(),
                    Duration.ofMinutes(5),
                    1L,
                    false);

            Mockito
                    .when(notificationsService.update(request))
                    .thenReturn(notification);

            mvc.perform(put("/notifications")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", equalTo(notification.getId()), Long.class))
                    .andExpect(jsonPath("$.type", equalTo(notification.getType())))
                    .andExpect(jsonPath("$.content", equalTo(notification.getContent())))
                    .andExpect(jsonPath("$.time_to_show", equalTo(notification.getTime_to_show().toString())))
                    .andExpect(jsonPath("$.interval_to_repeat", equalTo(notification.getInterval_to_repeat().toString())))
                    .andExpect(jsonPath("$.userId", equalTo(notification.getUserId()), Long.class))
                    .andExpect(jsonPath("$.immediately", equalTo(notification.getImmediately())));
        }

        // @TODO: add more tests
    }

    @Nested
    class DeleteNotification {
        @Test
        void deleteNotification() {
            // @TODO: impl
        }

        @Test
        void deleteNotifications() {
            // @TODO: impl
        }
    }

    @Nested
    class GetNotification {
        @Test
        void getNotificationsFullData() {
            // @TODO: impl
        }
    }
}