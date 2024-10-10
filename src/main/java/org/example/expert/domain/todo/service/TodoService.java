package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.request.TodoSearchReqeust;
import org.example.expert.domain.todo.dto.request.TodosGetRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional // Lv 1 - 1
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail(), user.getNickName())
        );
    }

    public Page<TodoResponse> getTodos(TodosGetRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        String weather = request.getWeather();
        LocalDateTime startModifiedDate = request.getStartModifiedDate() != null ? request.getStartModifiedDate().atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime endModifiedDate = request.getEndModifiedDate() != null ? request.getEndModifiedDate().atTime(23, 59, 59, 999999999) : LocalDateTime.MAX;

        log.info("startModifiedDate = {}", startModifiedDate);
        log.info("endModifiedDate = {}", endModifiedDate);

        Page<Todo> todos = todoRepository.findByWeatherOrModifiedDate(weather, startModifiedDate, endModifiedDate, pageable);

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail(), todo.getUser().getNickName()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail(), user.getNickName()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    // Lv 3 - 10 : QueryDSL 을 사용하여 검색 기능 만들기
    public Page<TodoSearchResponse> searchTodos(TodoSearchReqeust reqeust, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return todoRepository.searchTodos(reqeust, pageable);
    }

}
