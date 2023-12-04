package project.footprint.api.user.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.footprint.api.user.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @DisplayName("등록한 email로 사용자를 조회한다.")
    @Test
    void findByEmail() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";

        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileUrl(profileUrl)
                .build();

        userRepository.save(user);

        // when
        User findMember = userRepository.findByEmail(email);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember).extracting("email", "password", "nickname", "profileUrl")
                .contains(email, password, nickname, profileUrl);
    }

}