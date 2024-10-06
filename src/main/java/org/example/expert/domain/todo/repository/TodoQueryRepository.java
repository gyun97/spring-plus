package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchReqeust;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoQueryRepository {
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    // Lv 3 - 10 : QueryDSL 을 사용하여 검색 기능 만들기
    Page<TodoSearchResponse> searchTodos(TodoSearchReqeust reqeust, Pageable pageable);
}
