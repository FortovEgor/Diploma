package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateNotificationRequest;
import fortov.egor.diploma.dto.UpdateNotificationRequest;
import fortov.egor.diploma.exception.NotFoundException;
import fortov.egor.diploma.storage.NotificationsStorage;
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
    private final NotificationsStorage repo;
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
        Long notificationId = request.getId();
        Notification notification = repo.findById(notificationId);
        if (notification == null) {
            throw new NotFoundException("Уведомление с id = " + notificationId + " не найдено");
        }
        if (request.getType() == null) {
            request.setType(notification.getType());
        }
        if (request.getContent() == null) {
            request.setContent(notification.getContent());
        }
        if (request.getTime_to_show() == null) {
            request.setTime_to_show(notification.getTime_to_show());
        }
        if (request.getInterval_to_repeat() == null) {
            request.setInterval_to_repeat(notification.getInterval_to_repeat());
        }
        if (request.getUserId() == null) {
            request.setUserId(notification.getUserId());
        }
        if (request.getImmediately() == null) {
            request.setImmediately(notification.getImmediately());
        }
        Notification notificationFromRequest = mapper.toNotification(request);
        Notification notificationResult = repo.save(notificationFromRequest);
        manager.updateNotification(notificationResult);
        return notificationResult;
    }

    @Transactional
    public void delete(Long notificationId) {
        log.info("deleting notification with id = {}", notificationId);
        Notification notification = repo.findById(notificationId);
        if (notification == null) {
            throw new NotFoundException("Уведомление с id = " + notificationId + " не найдено");
        }
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
        List<Notification> notifications = repo.findAllById(notificationIds);
        if (notifications == null) {
            throw new NotFoundException("Failed to find any notifications with id from " + notificationIds);
        }
        return notifications;
    }
}
