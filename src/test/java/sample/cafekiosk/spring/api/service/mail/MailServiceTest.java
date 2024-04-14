package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailClient;
import sample.cafekiosk.spring.domain.history.MailSendHistory;
import sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {


//    @Mock
//    private MailClient mailClient;

    @Spy
    private MailClient mailClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;


    @DisplayName("메일을 전송한다.")
    @Test
    void sendEmail() {

        //given
        //MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
        //MailClient mailClient = mock(MailClient.class);
        //MailService mailService = new MailService(mailClient, mailSendHistoryRepository);
//        when(mailService.sendEmail(anyString(), anyString(), anyString(), anyString())).thenReturn(
//            true);
        doReturn(true)
            .when(mailClient)
            .sendEmail(anyString(), anyString(), anyString(), anyString());



        //when
        boolean result = mailService.sendEmail("", "", "", "");

        //then
        assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }
}