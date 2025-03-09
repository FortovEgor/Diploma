package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateNotificationRequest;
import fortov.egor.diploma.dto.UpdateNotificationRequest;
import fortov.egor.diploma.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationsRepository repo;
    private final NotificationsMapper mapper;
    private final NotificationsManager manager;

    @Transactional
    public Notification create(CreateNotificationRequest request) {
        log.info("creating new notification with data {}", request);
        Notification notificationFromRequest = mapper.toNotification(request);
        Notification notification = repo.save(notificationFromRequest);
        manager.registerNotification(notification);
        return notification;
    }

    @Transactional
    public Notification update(UpdateNotificationRequest request) {
        log.info("updating new notification with data {}", request);
        Notification notificationFromRequest = mapper.toNotification(request);
        Notification notification = repo.save(notificationFromRequest);
        manager.updateNotification(notification);
        return notification;
    }

    @Transactional
    public void delete(Long notificationId) {
        log.info("deleting notification with id = {}", notificationId);
        Notification notification = repo.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Уведомление с id = " + notificationId + " не найдено"));
        log.debug("deleting notification with id = {} from DB", notificationId);
        repo.deleteById(notificationId);
        log.debug("deleting notification with id = {} from manager", notificationId);
        manager.deleteNotification(notificationId);
    }

    @Transactional
    public void delete(List<Long> notificationIds) {
        log.info("deleting notification with ids = {}", notificationIds);
        List<Notification> notifications = repo.findAllById(notificationIds);
        if (notifications.isEmpty()) {
            throw new NotFoundException("Ни одного уведомления с id из " + notificationIds + " не найдено");
        }
        log.debug("deleting notifications with id = {} from DB", notificationIds);
        repo.deleteAllById(notificationIds);
        log.debug("deleting notifications with id = {} from manager", notificationIds);
        manager.deleteNotifications(notificationIds);
    }

    public List<Notification> get(List<Long> notificationIds) {
        log.info("getting notifications with ids = {}", notificationIds);
        return repo.findAllById(notificationIds);
    }
}
