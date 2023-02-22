package ru.innopolis.process_automation.process;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.community.process_test_coverage.junit4.platform7.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.*;
import org.mockito.MockitoAnnotations;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;


public class SelectionProcessTest {

  @Rule
  @ClassRule
  public static final ProcessEngineRule engine = TestCoverageProcessEngineRuleBuilder.create()
      .withDetailedCoverageLogging()
      .build();


  private static final String PROCESS_KEY = "internship-subprocess-1";

  @Before
  public void setUp() {
    JavaDelegate delegate = context -> {
    };
    MockitoAnnotations.openMocks(this);
    Mocks.register("DelegateSendReminder", delegate);
    Mocks.register("DelegateUpdateStatus", delegate);
  }

  @After
  public void tearDown() {
    Mocks.reset();
  }

  @Test
  public void testDeployment() {

  }

  @Test
  @Deployment(resources = {"bpmn/internship_subprocess_1.bpmn"})
  public void testHappyPath_isSuccessfullyExecuted() {
    ProcessInstance instance = runtimeService().createProcessInstanceByKey(PROCESS_KEY)
        .execute();
    assertThat(instance).isWaitingAt("Activity_1qjtj7b");
    complete(task());
    assertThat(instance).isWaitingAt("Event_1vn87pk");
    execute(job());
    complete(task());
    assertThat(instance).hasPassed("Activity_1quae69", "Event_1vn87pk", "Activity_1nti81y", "Activity_0jdnuwa", "Activity_0zqpj1k");
  }
}
