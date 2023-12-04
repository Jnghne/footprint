package project.footprint.api.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

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
}