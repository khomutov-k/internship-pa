package ru.innopolis.process_automation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.innopolis.process_automation.dto.StudentDto;
import ru.innopolis.process_automation.entity.ProcessCorrelation;
import ru.innopolis.process_automation.entity.Student;
import ru.innopolis.process_automation.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService service;
    @Autowired
    private StudentRepository repository;
    Random random = new Random();

    @Test
    void behaviour_logInFirstTime_saveDataSuccessfully() {
        String knownStudentId = UUID.randomUUID().toString();
        Long randomChatId = random.nextLong();

        StudentDto dto = StudentDto.builder()
                .studentCardId(knownStudentId)
                .chatId(randomChatId)
                .build();
        service.subscribeToService(dto);

        Optional<Student> studentOptional = repository.findByStudentCardId(knownStudentId);
        assertThat(studentOptional.isPresent()).isTrue();
        assertThat(studentOptional.get().getChatId()).isEqualTo(randomChatId);

        Optional<Student> chatOptional = repository.findByChatId(randomChatId);
        assertThat(chatOptional.isPresent()).isTrue();
        assertThat(chatOptional.get().getStudentCardId()).isEqualTo(knownStudentId);
    }

    @Test
    void behaviour_changeStudentId_changeDataSuccessfully() {
        Long randomChatId = random.nextLong();
        shouldAlreadyExist(randomChatId);

        String newStudentId = UUID.randomUUID().toString();
        StudentDto dto = StudentDto.builder()
                .studentCardId(newStudentId)
                .chatId(randomChatId)
                .build();
        service.subscribeToService(dto);

        Optional<Student> studentOptional = repository.findByStudentCardId(newStudentId);
        assertThat(studentOptional.isPresent()).isTrue();
        assertThat(studentOptional.get().getChatId()).isEqualTo(randomChatId);

        assertThat(repository.findAllByChatId(randomChatId).size()).isEqualTo(1);
    }
    
    @Test
    void regress_saveCorrelation_canRetrieveSuccessfully() {
        Long randomChatId = random.nextLong();
        shouldAlreadyExist(randomChatId);

        Optional<Student> studentOptional = repository.findByChatId(randomChatId);
        String randomProcessId = UUID.randomUUID().toString();
        String randomBusinessKey = UUID.randomUUID().toString();
        
        service.saveCorrelation(studentOptional.get(), randomProcessId, randomBusinessKey);

        List<ProcessCorrelation> processCorrelations = service.getProcessCorrelations(randomChatId);
        assertThat(processCorrelations).isNotEmpty();
        ProcessCorrelation actual = processCorrelations.iterator().next();
        assertThat(actual.getBusinessKey()).isEqualTo(randomBusinessKey);
        assertThat(actual.getProcessId()).isEqualTo(randomProcessId);

        assertThat(repository.findAllByChatId(randomChatId)).hasSize(1);
    }

    private void shouldAlreadyExist(Long chatId) {
        String knownStudentId = UUID.randomUUID().toString();
        StudentDto dto = StudentDto.builder()
                .studentCardId(knownStudentId)
                .chatId(chatId)
                .build();
        service.subscribeToService(dto);
    }
}
