package project.footprint.api.user.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.util.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("패스워드를 암호화 할 수 있다.")
    @Test
    void bcryptPassword() {
        // given
        String password = "beforePw";
        User user = User.builder()
                .password(password).build();

        // when
        user.encryptPassword(passwordEncoder);

        String encryptedPassword = user.getPassword();

        // then
        assertThat(encryptedPassword).isNotSameAs(password);
    }

    @DisplayName("of 메소드를 통해 User 객체를 생성할 수 있다. 단, 선택값인 컬럼은 null로 설정해야 한다")
    @Test
    void ofTest() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String username = "이종현";
        String nickname = "testNick";

        // when
        User user = User.of(email, password, username, nickname, null, null, null, null, null);

        // then
        assertThat(user).isNotNull();
        assertThat(user).extracting("email","password","username","nickname","provider","providerId", "profileUrl")
                .contains(email,password,username,nickname,null,null,null);
    }
    @DisplayName("columnDefault 어노테이션 정상 동작 테스트")
    @Test
    void columnDefaultTest() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String username = "이종현";
        String nickname = "testNick";

        User user = User.of(email, password, username, nickname, null, null, null, null, null);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // when
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(" 멤버가 없음 "));

        System.out.println("findUser = " + findUser.getRole());

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser).extracting("role")
                .isEqualTo(RoleType.USER);
    }
}