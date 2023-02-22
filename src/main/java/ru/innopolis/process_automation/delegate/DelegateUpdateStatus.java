package ru.innopolis.process_automation.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.innopolis.process_automation.entity.Student;
import ru.innopolis.process_automation.repository.StudentRepository;
import ru.innopolis.process_automation.service.StudentService;

import java.util.List;
import java.util.Optional;

@Component("DelegateUpdateStatus")
public class DelegateUpdateStatus implements JavaDelegate {
  StudentRepository studentRepository;
  StudentService service;

  public DelegateUpdateStatus(StudentRepository studentRepository, StudentService service) {
    this.studentRepository = studentRepository;
    this.service = service;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = (String) execution.getVariable("studentList");
    var studentIdList = mapper.readValue(json, new TypeReference<List<String>>() {
    });
    studentIdList.forEach(studentId -> {
      Optional<Student> studentOptional = studentRepository.findByStudentCardId(studentId);
      studentOptional.ifPresent(student -> service.saveCorrelation(student.getChatId(), execution.getProcessInstance().getId(), execution.getProcessBusinessKey()));
    });
  }

}
