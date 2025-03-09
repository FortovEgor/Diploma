package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateNotificationRequest;
import fortov.egor.diploma.dto.UpdateNotificationRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NotificationsMapper {
    Notification toNotification(CreateNotificationRequest request);

    Notification toNotification(UpdateNotificationRequest request);
}
