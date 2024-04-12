package sample.cafekiosk.spring.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailClient {

    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {

        log.info("메일 발송!!");
        if (1 == 1) {

            throw new IllegalArgumentException("메일 전송");
        }
        return true;
    }

}
