package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodosGetRequest {

    private String weather;
    private LocalDate startModifiedDate;
    private LocalDate endModifiedDate;
}
