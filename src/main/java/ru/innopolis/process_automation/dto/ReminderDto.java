package ru.innopolis.process_automation.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReminderDto {
    String message;
    Long chatId;
}
