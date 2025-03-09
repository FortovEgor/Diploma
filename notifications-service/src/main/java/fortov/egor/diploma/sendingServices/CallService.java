package fortov.egor.diploma.sendingServices;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import fortov.egor.diploma.user.dto.UserFullInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class CallService extends SendingServiceTemplate {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;


    CallService() {
        Twilio.init(accountSid, authToken);
    }
    @Override
    public void send(UserFullInfoDto receiver, String content) {
        Call call = Call.creator(
                new PhoneNumber(receiver.getPhone()),
                new PhoneNumber(twilioPhoneNumber),
                new com.twilio.type.Twiml("<Response><Say>" + content + "</Say></Response>")
        ).create();

        System.out.println("Call initiated: " + call.getSid());
    }
}
