package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.UserFullInfoDto;

public interface SendingService {
    void send(UserFullInfoDto receiver, String content);

    String getType();
}
