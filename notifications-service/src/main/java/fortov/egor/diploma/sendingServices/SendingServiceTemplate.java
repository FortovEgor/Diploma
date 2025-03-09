package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.User;
import fortov.egor.diploma.user.dto.UserFullInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public abstract class SendingServiceTemplate implements SendingService {
    @Getter
    private String type = "UNKNOWN";

    @Override
    public void send(UserFullInfoDto receiver, String content) {
        log.info("sending notification with content {} to {} via sender of type = {}",
                content, receiver, this.getType());
    }
}
