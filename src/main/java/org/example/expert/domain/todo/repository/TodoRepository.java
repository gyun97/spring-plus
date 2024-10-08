package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodosGetRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoQueryRepository{

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t" +
            " LEFT JOIN FETCH t.user u " +
            "WHERE (t.weather = :weather OR :weather IS NULL) " +
            "OR t.modifiedAt BETWEEN :startModifiedDate AND :endModifiedDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherOrModifiedDate(String weather, LocalDateTime startModifiedDate, LocalDateTime endModifiedDate, Pageable pageable);

}
