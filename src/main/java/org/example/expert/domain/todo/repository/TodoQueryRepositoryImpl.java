package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchReqeust;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepositoryImpl implements TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    // Lv 2- 8 :findByIdWithUser 메서드 Querydsl로 전환
    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo todo = queryFactory
                .selectFrom(QTodo.todo)
                .leftJoin(QTodo.todo.user, user).fetchJoin()
                .where(QTodo.todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(todo);
    }

    // Lv 3 - 10 : QueryDSL 을 사용하여 검색 기능 만들기
    @Override
    public Page<TodoSearchResponse> searchTodos(TodoSearchReqeust reqeust, Pageable pageable) {

        List<TodoSearchResponse> results = queryFactory
                .select(Projections.fields(TodoSearchResponse.class, // 필드 직접 주입 방식으로 프로젝션 반환 방법 선택
                        todo.title,
                        todo.managers.size().as("managerCount"),
                        todo.comments.size().as("commentCount")
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        titleContains(reqeust.getTitle()),
                        managerNickNameContains(reqeust.getNickName()),
                        createdAtBetween(reqeust.getStartCreatedAt(), reqeust.getEndCreatedAt())
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 검색된 일정 수(페이징 처리)
        long totalCount = queryFactory
                .select(todo.count())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(
                        titleContains(reqeust.getTitle()),
                        managerNickNameContains(reqeust.getNickName()),
                        createdAtBetween(reqeust.getStartCreatedAt(), reqeust.getEndCreatedAt())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, totalCount);

    }
    // 제목 검색 조건
    private BooleanExpression titleContains(String title) {
        return title != null ? todo.title.containsIgnoreCase(title) : null;
    }

    // 담당자 별명 검색 조건
    private BooleanExpression managerNickNameContains(String nickName) {
        return nickName != null ? manager.user.nickName.containsIgnoreCase(nickName) : null;
    }

    // 일정 생성일 범위 검색 조건
    private BooleanExpression createdAtBetween(LocalDate startCreatedAt, LocalDate endCreatedAt) {
        LocalDateTime startDate = startCreatedAt != null ? startCreatedAt.atStartOfDay() : null;
        LocalDateTime endDate = endCreatedAt != null ? endCreatedAt.atTime(23, 59, 59) : null;

        // 둘 다 null이면 일정 조건은 아예 생략된 거니까 null 반환해서 where절에서 날짜 조건 없애버린다.
        if (startDate == null && endDate == null) {
            return null;
        }

        return todo.createdAt.between(startDate, endDate);
    }
}

