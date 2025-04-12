package fortov.egor.diploma.sendingServices;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import fortov.egor.diploma.user.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Service
public class CallService extends SendingServiceTemplate {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;


    CallService() {
        super("call");
        try {
            Twilio.init(accountSid, authToken);
        } catch (RuntimeException e) {
            log.error("Failed to init Twilio connection: " + e +
                    " | call & sms notifications will not be delivered");
        }
    }
    @Override
    public void send(UserFullInfoDto receiver, String content) {
        Call call = Call.creator(
                new PhoneNumber(receiver.getPhone()),
                new PhoneNumber(twilioPhoneNumber),
                new com.twilio.type.Twiml("<Response><Say voice=\"woman\">" + content + "</Say><Play>http://demo.twilio.com/docs/classic.mp3</Play></Response>")
                // <Response>
                //<Say voice="woman">Thanks for trying our documentation. Enjoy!</Say>
                //<Play>http://demo.twilio.com/docs/classic.mp3</Play>
                //</Response>
        ).create();

        System.out.println("Call initiated: " + call.getSid());
    }
}
