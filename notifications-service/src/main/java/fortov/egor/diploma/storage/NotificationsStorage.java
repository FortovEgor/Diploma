package fortov.egor.diploma.storage;

import fortov.egor.diploma.Notification;

import java.util.List;

public interface NotificationsStorage {
    Notification save(Notification notification);
    void deleteById(Long NotificationId);

    void deleteAllById(List<Long> notificationsIds);

    Notification findById(Long notificationId);

    List<Notification> findAllById(List<Long> notificationsIds);
}
