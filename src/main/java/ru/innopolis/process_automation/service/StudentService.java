package ru.innopolis.process_automation.service;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.innopolis.process_automation.dto.StudentDto;
import ru.innopolis.process_automation.entity.ProcessCorrelation;
import ru.innopolis.process_automation.entity.Student;
import ru.innopolis.process_automation.mapper.StudentMapper;
import ru.innopolis.process_automation.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    public static final String STATUS_PROCESS_VARIABLE = "status";
    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final RuntimeService runtimeService;

    RepositoryService processService;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, RuntimeService runtimeService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.runtimeService = runtimeService;
    }

    public void subscribeToService(StudentDto dto) {
        studentRepository.findByChatId(dto.getChatId())
                .ifPresentOrElse(student -> {
                    student.setUnsubscribed(false);
                    student.setStudentCardId(dto.getStudentCardId());
                    studentRepository.save(student);
                }, () -> saveStudentToService(dto));
    }

    @Transactional
    public String getStatus(Long chatId, String businessKey) {
        Student student = studentRepository.findByChatId(chatId).orElseThrow();
        String processId = student.getProcessCorrelations().stream()
                .filter(corr -> corr.getBusinessKey().equalsIgnoreCase(businessKey))
                .map(ProcessCorrelation::getProcessId)
                .findAny().orElseThrow();

        return (String) runtimeService.createVariableInstanceQuery()
                .variableName(STATUS_PROCESS_VARIABLE)
                .processInstanceIdIn(processId)
                .list()
                .stream()
                .map(VariableInstance::getValue)
                .findAny().orElseThrow();

    }

    public void saveCorrelation(Student student, String processId, String businessKey) {
        ProcessCorrelation correlation = new ProcessCorrelation();
        correlation.setStudent(student);
        correlation.setProcessId(processId);
        correlation.setBusinessKey(businessKey);

        student.getProcessCorrelations().add(correlation);
        studentRepository.save(student);
    }

    private void saveStudentToService(StudentDto dto) {
        if (studentRepository.findByStudentCardId(dto.getStudentCardId()).isEmpty()) {
            Student student = studentMapper.fromStudentDto(dto);
            studentRepository.save(student);
        }
    }
    @Transactional
    public List<ProcessCorrelation> getProcessCorrelations(Long randomChatId) {
        Optional<Student> studentWithCorrelationOptional = studentRepository.findByChatId(randomChatId);
        List<ProcessCorrelation> processCorrelations = new ArrayList<>(studentWithCorrelationOptional.get().getProcessCorrelations());
        return processCorrelations;
    }
    public void unsubscribeFromService(StudentDto dto) {
        studentRepository.findByChatId(dto.getChatId())
                .ifPresent(student -> {
                    student.setUnsubscribed(true);
                    studentRepository.save(student);
                });
    }
}
