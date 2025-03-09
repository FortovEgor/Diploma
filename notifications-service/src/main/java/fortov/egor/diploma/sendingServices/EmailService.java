package fortov.egor.diploma.sendingServices;

import fortov.egor.diploma.user.dto.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Service
public class EmailService extends SendingServiceTemplate {

    private static final String MESSAGE_SUBJECT = "ALARM FROM ARGUS";
    private static final String SENDER_MAIL = "argusnotify@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    EmailService() {
        super("email");
    }
    @Override
    public void send(UserFullInfoDto receiver, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver.getEmail());
        message.setSubject(MESSAGE_SUBJECT);
        message.setText(content);
        message.setFrom(SENDER_MAIL);
        log.debug("sending message {}", message);
        mailSender.send(message);
    }
}
