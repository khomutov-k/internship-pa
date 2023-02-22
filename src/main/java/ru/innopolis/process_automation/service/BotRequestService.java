package ru.innopolis.process_automation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.innopolis.process_automation.dto.ReminderDto;

@Service
public class BotRequestService {
  @Value("process.app.botUrl")
  String botUrl;
  RestTemplate template;

  public BotRequestService(RestTemplate template) {
    this.template = template;
  }

  public void sendMessageByChatId(String message, Long chatId) {
    ReminderDto dto = ReminderDto.builder()
        .message(message)
        .chatId(chatId)
        .build();

    template.postForEntity(botUrl, dto, Void.class);
  }
}
