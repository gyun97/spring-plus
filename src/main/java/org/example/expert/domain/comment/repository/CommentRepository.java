package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.todo.id = :todoId") // Lv 2 - 7 : Fetch Join으로 N + 1 해결
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
