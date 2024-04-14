package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailClient;
import sample.cafekiosk.spring.domain.history.MailSendHistory;
import sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;

@Service
@RequiredArgsConstructor
public class MailService {

    @Autowired
    private final MailClient mailClient;

    @Autowired
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {

        boolean result = mailClient.sendEmail(
            fromEmail
            , toEmail
            , subject
            , content);

        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .content(content)
                .build()
            );
            return true;
        }
        return false;
    }

}
