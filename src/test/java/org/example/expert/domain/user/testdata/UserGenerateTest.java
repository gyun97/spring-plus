package org.example.expert.domain.user.testdata;

import org.example.expert.config.S3Config;
import org.example.expert.domain.s3.S3Uploader;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
//@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yml")
public class UserGenerateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private S3Config s3Config;

    @MockBean
    private S3Uploader s3Uploader;

    private final int BATCH_SIZE = 1000; // 배치 사이즈 설정
    private final int TOTAL_RECORDS = 1000000; // 총 100만 건


    /**
     * JDBCTemplate의 batchUpdate() 메서드 이용해서 유저 데이터 100만 건 삽입
     * 성능 11 ~ 12초
     */
    @Test
    @Commit
    @Transactional
    void insertUsersByBatchUpdate() {
        Set<String> uniqueNicknames = new HashSet<>(); // 닉네임 중복 방지를 위한 Set
        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 0; i < TOTAL_RECORDS; i++) {
            String nickName = generateUniqueRandomNickName(uniqueNicknames);
            String email = "user" + i + "@example.com";
            String password = "password" + i;
            String userRole = "ROLE_USER";

            // 배치 데이터 준비
            Object[] userData = new Object[]{email, password, userRole, nickName};
            batchArgs.add(userData);

            // 배치 사이즈에 도달하면 DB에 Insert
            if (i % BATCH_SIZE == 0) {
                executeBatchInsert(batchArgs);
                batchArgs.clear();
            }
        }

        // 혹시라도 남아있는 데이터 마저 저장
        if (!batchArgs.isEmpty()) {
            executeBatchInsert(batchArgs);
        }

        System.out.println("닉네임 중복 없는 유저 테스트 데이터 " + countUserData() + " 건 저장 성공!");
    }

    private void executeBatchInsert(List<Object[]> batchArgs) {
        String sql = "INSERT INTO users (email, password, user_role, nickname) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }


    /**
     * JPA 단순하게 데이터 삽입 - 시간 너무 오래 걸려서 사용 안 하기로 함
     */
    @Test
    @Transactional
    public void insertUsers() {

        Set<String> uniqueNicknames = new HashSet<>(); // 중복 방지를 위한 Set
        Random random = new Random();

        for (int i = 0; i < TOTAL_RECORDS; i++) {
            String email = "user" + i + "@example.com";
            String password = "password" + i;
            String nickname = generateUniqueRandomNickName(uniqueNicknames);
            UserRole userRole = UserRole.ROLE_USER;

            User user = new User(email, password, userRole, nickname);
            userRepository.save(user); // JPA의 save 메서드로 저장

        }
    }

    // 랜덤 닉네임 중복되지 않으면 저장하는 메서드
    private String generateUniqueRandomNickName(Set<String> uniqueNicknames) {
        String nickName;
        do {
//            nickName = GenerateUserNickName.generateRandomNickNameByAscii();
//            nickName = GenerateUserNickName.generateRandomNickNameByRandomStringUtils();
            nickName = GenerateUserNickName.generateRandomNickNameByCollectionShuffle();
        } while (!uniqueNicknames.add(nickName)); // 중복 시 닉네임 다시 생성
        return nickName;
    }

    // DB에 데이터 잘 들어갔는지 데이터 개수 확인
    public int countUserData() {
        String sql = "SELECT COUNT(*) FROM users";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


    /**
     * 닉네임 조회 검사
     */
    @Test
    @Transactional(readOnly = true)
    void findUserByNickName() {
        String userNickName = "몰두한 얼룩말 82525";
        Optional<User> byNickName = userRepository.findByNickName(userNickName);
        assertThat(byNickName.get().getNickName()).isEqualTo(userNickName);
    }



}





