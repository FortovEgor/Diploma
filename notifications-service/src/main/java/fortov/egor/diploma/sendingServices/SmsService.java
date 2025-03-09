package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.dto.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Slf4j
@Service
public class SmsService extends SendingServiceTemplate {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    public void sendSms(String to, String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(to), // номер получателя
                new PhoneNumber(twilioPhoneNumber), // ваш номер Twilio
                messageBody // текст сообщения
        ).create();

        System.out.println("SMS sent: " + message.getSid());
    }


    SmsService() {
        Twilio.init(accountSid, authToken);
    }
    @Override
    public void send(UserFullInfoDto receiver, String content) {
        Message message = Message.creator(
                new PhoneNumber(receiver.getPhone()),
                new PhoneNumber(twilioPhoneNumber),
                content
        ).create();  // todo: messege.send() ?
        log.debug("message with content {} sent to {}", content, receiver);
    }
}
