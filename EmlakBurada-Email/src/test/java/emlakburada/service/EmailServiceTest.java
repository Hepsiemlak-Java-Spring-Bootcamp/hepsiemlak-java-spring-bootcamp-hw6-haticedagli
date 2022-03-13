package emlakburada.service;

import com.sun.mail.smtp.SMTPTransport;
import emlakburada.config.EmailConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.MessagingException;
import javax.mail.Session;

import java.util.Properties;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private EmailConfig config;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void send_should_call_send_message() throws MessagingException {
        //given
        BDDMockito.given(config.getSmtpServer()).willReturn("smtp.gmail.com");
        BDDMockito.given(config.getSmtpPort()).willReturn("587");
        BDDMockito.given(config.getUsername()).willReturn("emlakburada.patika@gmail.com");
        BDDMockito.given(config.getPassword()).willReturn("EmlakBurada2022");
        BDDMockito.given(config.getFrom()).willReturn("emlakburada.patika@gmail.com");
        BDDMockito.given(config.getSubject()).willReturn("EmlakBurada'ya Hoş Geldiniz");

        Session session = mock(Session.class);
        SMTPTransport transport = mock(SMTPTransport.class);

        MockedStatic<Session> mockedStatic = mockStatic(Session.class);

        mockedStatic
                .when(() -> Session.getInstance(any(Properties.class), any(javax.mail.Authenticator.class)))
                .thenReturn(session);

        BDDMockito
                .given(session.getProperties())
                .willReturn(System.getProperties());

        BDDMockito
                .given(session.getTransport(anyString()))
                .willReturn(transport);

        //when
        emailService.send("haticeetoglu03@gmail.com");

        //then
        Mockito
                .verify(transport, times(1))
                .sendMessage(any(), any());
    }
}