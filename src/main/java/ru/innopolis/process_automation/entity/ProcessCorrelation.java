package ru.innopolis.process_automation.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "process_correlation")
public class ProcessCorrelation {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "process_id", nullable = false)
  private String processId;

  @Column(name = "business_key", nullable = false)
  private String businessKey;

  @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @PrePersist
  public void setUuid() {
    setId(UUID.randomUUID());
  }
}
