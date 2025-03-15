package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Для сервисов реализован паттерн "Декоратор":
// он позволит легко добавлять новые сервисвы нотификаций и комбинировать их

@Slf4j
@Service
public class SendingManager extends SendingServiceTemplate {
    List<SendingService> sendingServices;

    @Autowired
    SendingManager(List<SendingService> sendingServices) {
        this.sendingServices = sendingServices;
    }

    @Override
    public void send(UserFullInfoDto receiver, String context) {
        log.info("sending notification to {}, content: {} via {} senders", receiver, context, sendingServices.size());
        for (SendingService service : sendingServices) {
            service.send(receiver, context);
        }
    }
}
