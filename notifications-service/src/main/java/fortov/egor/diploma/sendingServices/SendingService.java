package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.User;
import fortov.egor.diploma.user.dto.UserFullInfoDto;

public interface SendingService {
    void send(UserFullInfoDto receiver, String content);

    String getType();
}
