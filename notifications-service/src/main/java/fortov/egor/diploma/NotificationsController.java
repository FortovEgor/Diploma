package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateNotificationRequest;
import fortov.egor.diploma.dto.UpdateNotificationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        return service.create(request);
    }

    @PutMapping
    public Notification updateNotification(@Valid @RequestBody UpdateNotificationRequest request) {
        return service.update(request);
    }

    @DeleteMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void deleteNotification(@PathVariable Long notificationId) {
        service.delete(notificationId);
        return null;
    }

    @DeleteMapping("/array/{notificationIds}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void deleteNotifications(@PathVariable List<Long> notificationIds) {
        service.delete(notificationIds);
        return null;
    }

    @GetMapping("/{notificationIds}")
    public List<Notification> getNotificationsFullData(@PathVariable List<Long> notificationIds) {
        return service.get(notificationIds);
    }
}
