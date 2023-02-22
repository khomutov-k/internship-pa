package ru.innopolis.process_automation.controller;

import org.springframework.web.bind.annotation.*;
import ru.innopolis.process_automation.dto.StudentDto;
import ru.innopolis.process_automation.service.StudentService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @PostMapping("/api/v1/student/subscribe")
  @ResponseStatus(OK)
  public void subscribeToService(@RequestBody @Valid StudentDto dto) {
    studentService.subscribeToService(dto);
  }

  @GetMapping("/api/v1/process/status")
  @ResponseStatus(OK)
  public String subscribeToService(@RequestParam Long studentChatId, @RequestParam String businessKey) {
    return studentService.getStatus(studentChatId, businessKey);
  }

  @PostMapping("/api/v1/student/unsubscribe")
  @ResponseStatus(OK)
  public void unsubscribeToService(@RequestBody StudentDto dto) {
    studentService.unsubscribeFromService(dto);
  }
}
