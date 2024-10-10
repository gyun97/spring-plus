package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users",  indexes = @Index(name = "idx_nickname", columnList = "nickname"))
@BatchSize(size = 1000)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "nickname")
    private String nickName; // 유저 닉네임 컬럼 추가

    // 생성자: userRole을 UserRole로 받음
    public User(String email, String password, UserRole userRole, String nickName) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.nickName = nickName;
    }

    // AuthUser에서 User로 변환
    private User(Long id, String email, UserRole userRole, String nickName) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
        this.nickName = nickName;
    }

    // AuthUser로부터 User 객체 생성
    public static User fromAuthUser(AuthUser authUser) {
        // AuthUser의 authorities에서 첫 번째 권한을 UserRole로 변환
        UserRole userRole = UserRole.of(authUser.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse(UserRole.ROLE_USER.name())); // 기본값: ROLE_USER

        return new User(authUser.getUserId(), authUser.getEmail(), userRole, authUser.getNickName());
    }

    // 비밀번호 변경
    public void changePassword(String password) {
        this.password = password;
    }

    // 사용자 역할 업데이트
    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
