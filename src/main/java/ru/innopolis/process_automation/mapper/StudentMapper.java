package ru.innopolis.process_automation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.innopolis.process_automation.dto.StudentDto;
import ru.innopolis.process_automation.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "id", ignore = true)
    Student fromStudentDto(StudentDto dto);

    StudentDto toStudentDto(Student entity);
}