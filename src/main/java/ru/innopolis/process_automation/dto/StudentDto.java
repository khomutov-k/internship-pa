package ru.innopolis.process_automation.dto;

import lombok.*;

import javax.validation.constraints.Null;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentDto {
    private String studentCardId;
    @NonNull
    private Long chatId;
    private boolean unsubscribed;
}
