package ru.innopolis.process_automation.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import ru.innopolis.process_automation.dto.StudentDto;
import ru.innopolis.process_automation.repository.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentApiTest {
  public static final String TEST_CARD_ID = "test-card-id";
  @Autowired
  private TestRestTemplate template;
  @Autowired
  private StudentRepository repository;

  @Test
  void smoke_subscribeToService_canSubscribeSuccessfully() {
    StudentDto dto = StudentDto.builder()
        .studentCardId(TEST_CARD_ID)
        .chatId(123123L)
        .build();

    HttpStatus statusCode = template.postForEntity("/api/v1/student/subscribe", dto, null).getStatusCode();
    assertThat(statusCode).isEqualTo(HttpStatus.OK);

    repository.findByStudentCardId(TEST_CARD_ID)
        .orElseThrow(() -> new AssertionError("No student found by student card"));
  }
}
