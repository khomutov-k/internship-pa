package ru.innopolis.process_automation.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.innopolis.process_automation.repository.StudentRepository;
import ru.innopolis.process_automation.service.BotRequestService;

import java.util.List;

@Component("DelegateSendReminder")
public class DelegateSendReminder implements JavaDelegate {
    StudentRepository studentRepository;
    BotRequestService botRequestService;

    public DelegateSendReminder(StudentRepository studentRepository, BotRequestService botRequestService) {
        this.studentRepository = studentRepository;
        this.botRequestService = botRequestService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = (String) execution.getVariable("studentList");
        var studentList = mapper.readValue(json, new TypeReference<List<String>>(){});

        var reminderMessage = (String) execution.getVariable("reminderMessage");
        studentList.forEach(studentId -> {
            studentRepository.findByStudentCardId(studentId)
                    .ifPresent(stData -> botRequestService.sendMessageByChatId(reminderMessage,stData.getChatId()));
        });
    }
}