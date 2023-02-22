package ru.innopolis.process_automation.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "student")
public class Student {

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "student_card_id", nullable = false)
  private String studentCardId;

  @Column(name = "chat_id", nullable = false)
  private Long chatId;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProcessCorrelation> processCorrelations = new java.util.ArrayList<>();

  @Column(name = "unsubscribed")
  private boolean unsubscribed;

  @PrePersist
  public void setUuid() {
    setId(UUID.randomUUID());
  }
}
