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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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
        void createNotificationNullType() throws Exception {
            CreateNotificationRequest request = new CreateNotificationRequest(
                    null,
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
                    .andExpect(status().is4xxClientError());
        }

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

        @Test
        void createNotificationNullContent() throws Exception {
            CreateNotificationRequest request = new CreateNotificationRequest(
                    "some type",
                    null,
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
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void createNotificationWithNoUser() throws Exception {
            CreateNotificationRequest request = new CreateNotificationRequest(
                    "some type",
                    "some content",
                    LocalDateTime.now().toString(),
                    Duration.ofMinutes(5),
                    null,
                    false);

            Mockito
                    .when(notificationsService.create(request))
                    .thenReturn(notification);

            mvc.perform(post("/notifications")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }
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
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.id", equalTo(notification.getId()), Long.class))
                    .andExpect(jsonPath("$.type", equalTo(notification.getType())))
                    .andExpect(jsonPath("$.content", equalTo(notification.getContent())))
                    .andExpect(jsonPath("$.time_to_show", equalTo(notification.getTime_to_show().toString())))
                    .andExpect(jsonPath("$.interval_to_repeat", equalTo(notification.getInterval_to_repeat().toString())))
                    .andExpect(jsonPath("$.userId", equalTo(notification.getUserId()), Long.class))
                    .andExpect(jsonPath("$.immediately", equalTo(notification.getImmediately())));
        }
    }

    @Nested
    class DeleteNotification {
        @Test
        void deleteNotification() throws Exception {
            doNothing().when(notificationsService).delete(anyLong());

            mvc.perform(delete("/notifications/" + notification.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteNotifications() throws Exception {
            doNothing().when(notificationsService).delete(anyList());

            List<Long> notificationIds = Arrays.asList(1L, 2L, 3L);
            mvc.perform(delete("/notifications")
                            .content(mapper.writeValueAsString(notificationIds))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class GetNotification {
        @Test
        void getNotificationsFullData() throws Exception {
            Mockito
                    .when(notificationsService.get(anyList()))
                    .thenReturn(List.of(notification));

            String urlTemplate = "/notifications/" + notification.getId() + ",2,3";
            mvc.perform(get(urlTemplate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", equalTo(notification.getId()), Long.class))
                    .andExpect(jsonPath("$[0].type", equalTo(notification.getType())))
                    .andExpect(jsonPath("$[0].content", equalTo(notification.getContent())))
                    .andExpect(jsonPath("$[0].time_to_show", equalTo(notification.getTime_to_show().toString())))
                    .andExpect(jsonPath("$[0].interval_to_repeat", equalTo(notification.getInterval_to_repeat().toString())))
                    .andExpect(jsonPath("$[0].userId", equalTo(notification.getUserId()), Long.class))
                    .andExpect(jsonPath("$[0].immediately", equalTo(notification.getImmediately())));
        }
    }
}