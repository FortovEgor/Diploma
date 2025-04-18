package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.smsaero.SmsAero;

@Slf4j
@Service
public class SmsService extends SendingServiceTemplate {
    @Value("${aero-sms.email}")
    private String aeroEmail;

    @Value("${aero-sms.key}")
    private String aeroKey;

    private static String header = "Привет! На связи Argus. У нас для тебя уведомление. ";

    public void sendSms(String to, String messageBody) {
        String phone = formatPhoneNumber(to);
        if (phone == null) {
            log.error("sms to {} was not sent due to internal error while phone handling", to);
            return;
        }
        try {
            SmsAero client = new SmsAero(aeroEmail, aeroKey);
            JSONObject sendResult = client.SendSms(phone, header + messageBody);
            log.info("result of sending sms to {}: {}", to, sendResult);
        } catch (Exception e) {
            log.error("sending sms to {} failed: {}", to, e.getMessage());
        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Удаляем все символы, кроме цифр
        String cleaned = phoneNumber.replaceAll("[^\\d]", "");

        // Проверяем, начинается ли номер с 8 или 7 и заменяем на 7
        if (cleaned.startsWith("8")) {
            cleaned = "7" + cleaned.substring(1);
        } else if (cleaned.startsWith("7")) {

        } else if (cleaned.length() == 10) {
            // Если номер состоит из 10 цифр, добавляем 7 в начале
            cleaned = "7" + cleaned;
        } else {
            // Если номер не соответствует ожидаемым форматам, возвращаем null или выбрасываем исключение
            return null;
        }

        return cleaned;
    }

    SmsService() {
        super("sms");
    }
    @Override
    public void send(UserFullInfoDto receiver, String content) {
        sendSms(receiver.getPhone(), content);
    }
}
