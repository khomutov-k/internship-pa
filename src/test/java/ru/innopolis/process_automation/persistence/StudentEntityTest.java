package ru.innopolis.process_automation.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.innopolis.process_automation.entity.ProcessCorrelation;
import ru.innopolis.process_automation.entity.Student;
import ru.innopolis.process_automation.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataJpa
public class StudentEntityTest {
  @Autowired
  private StudentRepository studentRepository;

  @Test
  @Transactional
  void regress_saveCorrelation_canRetrieveSuccessfully() {
    ProcessCorrelation correlation = new ProcessCorrelation();
    correlation.setProcessId(UUID.randomUUID().toString());
    correlation.setBusinessKey(UUID.randomUUID().toString());

    Student student = new Student();
    student.setStudentCardId("123321");
    student.setChatId(123321L);
    correlation.setStudent(student);
    student.setProcessCorrelations(List.of(correlation));
    UUID id = studentRepository.saveAndFlush(student).getId();
    Optional<Student> studentOptional = studentRepository.findById(id);
    assertThat(studentOptional).isPresent();
    Student actual = studentOptional.get();
    assertThat(actual).hasNoNullFieldsOrProperties();
    assertThat(actual.getProcessCorrelations().iterator().next()).usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(correlation);
  }
}
