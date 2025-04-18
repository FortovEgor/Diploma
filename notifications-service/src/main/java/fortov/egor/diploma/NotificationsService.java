package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateNotificationRequest;
import fortov.egor.diploma.dto.UpdateNotificationRequest;
import fortov.egor.diploma.exception.DBException;
import fortov.egor.diploma.exception.NotFoundException;
import fortov.egor.diploma.storage.NotificationsStorage;
import fortov.egor.diploma.user.UserFullInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

        final long userId = request.getUserId();
        if (!userExists(userId)) {
            throw new NotFoundException("Нельзя назначить уведомление несуществующему пользователю (id = " +
                    userId + ")");
        }

        Notification notificationFromRequest = mapper.toNotification(request);

        Notification notification;
        try {
            notification = repo.save(notificationFromRequest);
        } catch (Exception e) {
            throw new DBException();
        }

        manager.registerNotification(notification);
        return notification;
    }

    @Transactional
    public Notification update(UpdateNotificationRequest request) {
        log.info("updating new notification with data {}", request);
        Long notificationId = request.getId();

        Notification notification;
        try {
            notification = repo.findById(notificationId);
        } catch (Exception e) {
            throw new DBException();
        }

        if (notification == null) {
            throw new NotFoundException("Уведомление с id = " + notificationId + " не найдено");
        }

        final long userId = request.getUserId();
        if (!userExists(userId)) {
            throw new NotFoundException("Нельзя назначить уведомление несуществующему пользователю (id = " +
                    userId + ")");
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

        Notification notificationResult;
        try {
            notificationResult = repo.save(notificationFromRequest);
        } catch (Exception e) {
            throw new DBException();
        }

        manager.updateNotification(notificationResult);
        return notificationResult;
    }

    @Transactional
    public void delete(Long notificationId) {
        log.info("deleting notification with id = {}", notificationId);

        Notification notification;
        try {
            notification = repo.findById(notificationId);
        } catch (Exception e) {
            throw new DBException();
        }

        if (notification == null) {
            throw new NotFoundException("Уведомление с id = " + notificationId + " не найдено");
        }
        log.debug("deleting notification with id = {} from DB", notificationId);

        try {
            repo.deleteById(notificationId);
        } catch (Exception e) {
            throw new DBException();
        }

        log.debug("deleting notification with id = {} from manager", notificationId);
        manager.deleteNotification(notificationId);
    }

    @Transactional
    public void delete(List<Long> notificationIds) {
        log.info("deleting notification with ids = {}", notificationIds);

        List<Notification> notifications;
        try {
            notifications = repo.findAllById(notificationIds);
        } catch (Exception e) {
            throw new DBException();
        }

        if (notifications.isEmpty()) {
            throw new NotFoundException("Ни одного уведомления с id из " + notificationIds + " не найдено");
        }
        log.debug("deleting notifications with id = {} from DB", notificationIds);

        try {
            repo.deleteAllById(notificationIds);
        } catch (Exception e) {
            throw new DBException();
        }

        log.debug("deleting notifications with id = {} from manager", notificationIds);
        manager.deleteNotifications(notificationIds);
    }

    public List<Notification> get(List<Long> notificationIds) {
        log.info("getting notifications with ids = {}", notificationIds);

        List<Notification> notifications;
        try {
            notifications = repo.findAllById(notificationIds);
        } catch (Exception e) {
            throw new DBException();
        }

        if (notifications == null) {
            throw new NotFoundException("Failed to find any notifications with id from " + notificationIds);
        }
        return notifications;
    }

    private boolean userExists(long userId) {
        ResponseEntity<UserFullInfoDto> response = manager.userClient.getUser(userId);
        return (response != null) && response.getStatusCode().is2xxSuccessful();
    }
}
