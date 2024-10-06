package org.example.expert.domain.todo.dto.request;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TodoSearchReqeust {

    private String title;
    private LocalDate startCreatedAt;
    private LocalDate endCreatedAt;
    private String nickName;

    public TodoSearchReqeust(String title, LocalDate startCreatedAt, LocalDate endCreatedAt, String nickName) {
        this.title = title;
        this.startCreatedAt = startCreatedAt;
        this.endCreatedAt = endCreatedAt;
        this.nickName = nickName;
    }
}
