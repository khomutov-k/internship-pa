package ru.innopolis.process_automation.process;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import ru.innopolis.process_automation.dto.StudentDto;
import ru.innopolis.process_automation.service.StudentService;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;


@SpringBootTest
@AutoConfigureDataJpa
@ExtendWith(CustomMockExtension.class)
public class ProcessIntegrationTest {
  public static final long CHAT_ID = 123L;
  private static final String PROCESS_KEY = "internship-pa";
  private static final String STUDENT_CARD_ID = "test-student-123";
  private static final String STUDENT_CARD_VARIABLE = "[\"test-student-123\"]";
  public static final String BUSINESS_KEY = "BK-INTERNSHIP-test";
  public static final String SENT_FOR_REVIEW = "The vacancies have been sent for review";

  ProcessEngine engine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
      .buildProcessEngine();

  @RegisterExtension
  ProcessEngineExtension extension = ProcessEngineExtension.builder()
      .useProcessEngine(engine)
      .build();

  @Autowired
  private StudentService studentService;

  @Test
  public void testDeployment() {

  }

  @Test
  @Deployment(resources = {"bpmn/internship-process-pa.bpmn"})
  public void testGetStatus_isSuccessfullyExecuted() {
    StudentDto studentDto = StudentDto.builder()
        .studentCardId(STUDENT_CARD_ID)
        .chatId(CHAT_ID)
        .build();

    studentService.subscribeToService(studentDto);

    ProcessInstance instance = runtimeService().createProcessInstanceByKey(PROCESS_KEY)
        .setVariable("studentList", STUDENT_CARD_VARIABLE)
        .businessKey(BUSINESS_KEY)
        .execute();


    assertThat(instance).isWaitingAt("Activity_1qjtj7b");
    complete(task());
    assertThat(instance).isWaitingAt("Event_1vn87pk");

    String status = studentService.getStatus(CHAT_ID, BUSINESS_KEY);
    Assertions.assertThat(status).isEqualTo(SENT_FOR_REVIEW);
  }
}
