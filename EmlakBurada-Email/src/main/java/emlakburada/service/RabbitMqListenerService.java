package emlakburada.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import emlakburada.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RabbitMqListenerService {

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = "${emlakburada.rabbitmq.queue}")
	public void receiveMessage(EmailMessage message) {
		log.info(message.toString());
		emailService.send(message.getEmail());
	}

}
