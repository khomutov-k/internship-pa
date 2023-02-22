package ru.innopolis.process_automation.process;

import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.innopolis.process_automation.delegate.DelegateSendReminder;
import ru.innopolis.process_automation.repository.StudentRepository;
import ru.innopolis.process_automation.service.BotRequestService;

public class CustomMockExtension implements BeforeAllCallback {
  @Mock
  BotRequestService botRequestService;
  @Mock
  StudentRepository studentRepo;
  @InjectMocks
  DelegateSendReminder sendReminder;

  public void beforeAll(ExtensionContext context) {
    MockitoAnnotations.openMocks(this);
    Mocks.register("DelegateSendReminder", sendReminder);
  }
}
