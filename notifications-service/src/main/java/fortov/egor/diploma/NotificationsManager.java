package fortov.egor.diploma;

import fortov.egor.diploma.sendingServices.SendingService;
import fortov.egor.diploma.user.dto.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NotificationsManager {
    Map<String, SendingService> typeSendingServices = new HashMap<>();
    Map<Long, Thread> notificationIdProcessor = new HashMap<>();

    UserClient userClient;

    @Autowired
    NotificationsManager(List<SendingService> sendingServices) {
        for (SendingService service : sendingServices) {
            typeSendingServices.put(service.getType(), service);
        }
        String types = typeSendingServices.keySet().toString();
        log.debug("detected service services types: {}", types);
    }

    public void registerNotification(Notification notification) {
        // HACK: call update in order not to CPU resources leak
        log.info("started processing notification {}", notification);
        updateNotification(notification);
    }

    private void processNotificationSending(Notification notification) throws InterruptedException {
        boolean firstIter = true;
        while (!Thread.currentThread().isInterrupted()) {
            final Long threadId = Thread.currentThread().threadId();
            log.debug("Thread {} is running...", threadId);
            log.info("Stated processing notification {}", notification);

            SendingService processingService = typeSendingServices.get(notification.getType());
            if (processingService == null) {
                log.error("failed to find processing service for notification = {}", notification);
                return;
            }

            final Long receiverId = notification.getUserId();
            UserFullInfoDto receiver = getUserById(receiverId);
            if (receiver == null) {
                log.error("Failed to find user with id = {}", receiverId);
                return;
            }

            if (firstIter) {
                if (notification.getImmediately().booleanValue()) {
                        processingService.send(receiver, notification.getContent());
                } else {
                    Duration timeToWaitForFirstSending =
                            Duration.between(notification.getTime_to_show(), LocalDateTime.now());
                    if (timeToWaitForFirstSending.isPositive()) {
                        Thread.sleep(timeToWaitForFirstSending);
                    }
                }
            } else {
                processingService.send(receiver, notification.getContent());
            }

            log.debug("notification {} sent", notification);
            firstIter = false;
            Thread.sleep(notification.getInterval_to_repeat());
        }
    }

    public void updateNotification(Notification notification) {
        log.info("updating notification {}", notification);
        if (notificationIdProcessor.containsKey(notification.getId())) {
            notificationIdProcessor.get(notification.getId()).interrupt();
        }
        Thread notificationThread = new Thread(() -> {
            try {
                processNotificationSending(notification);
            } catch (InterruptedException e) {
                log.debug("Processing of notification with id = " + notification.getId() + " was stopped");
            } finally {
                log.debug("Worker thread with id = " + Thread.currentThread().threadId() + " is cleaning up and exiting.");
            }
        });
        notificationIdProcessor.put(notification.getId(), notificationThread);
        notificationThread.start();
    }

    public void deleteNotification(Long notificationId) {
        log.info("deleting notification with id = {}", notificationId);
        if (notificationIdProcessor.containsKey(notificationId)) {
            notificationIdProcessor.get(notificationId).interrupt();
        }
    }

    public void deleteNotifications(List<Long> notificationIds) {
        for (Long notificationId : notificationIds) {
            deleteNotification(notificationId);
        }
    }

    private UserFullInfoDto getUserById(Long userId) {
         return userClient.getUser(userId);
    }
}
