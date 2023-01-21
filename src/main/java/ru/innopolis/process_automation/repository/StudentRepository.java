package ru.innopolis.process_automation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.innopolis.process_automation.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByStudentCardId(String studentCardId);

    Optional<Student> findByChatId(Long chatId);

    List<Student> findAllByChatId(Long chatId);
}
