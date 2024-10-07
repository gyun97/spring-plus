package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.log.enums.SaveManagerStatus;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "log")
@Slf4j
@Getter
public class Log extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long managerId;
    private Long todoId;
    private String message;

    @Enumerated(EnumType.STRING)
    private SaveManagerStatus status;

    public Log(Long managerId, Long todoId, String message, SaveManagerStatus status) {
        this.managerId = managerId;
        this.todoId = todoId;
        this.message = message;
        this.status = status;
    }
}
