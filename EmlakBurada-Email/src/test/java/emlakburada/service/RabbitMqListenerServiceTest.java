package emlakburada.service;

import emlakburada.dto.EmailMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RabbitMqListenerServiceTest {

    @InjectMocks
    private RabbitMqListenerService rabbitMqListenerService;

    @Mock
    private EmailService emailService;

    @Test
    public void receiveMessage_should_send_message(){
        //given
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setEmail("haticeetoglu03@gmail.com");

        //when
        rabbitMqListenerService.receiveMessage(emailMessage);

        //then
        Mockito
                .verify(emailService, times(1))
                .send(emailMessage.getEmail());
    }
}