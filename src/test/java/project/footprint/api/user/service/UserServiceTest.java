package project.footprint.api.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.footprint.api.user.dto.request.UserJoinRequest;
import project.footprint.api.user.dto.request.UserLoginRequest;
import project.footprint.api.user.entity.User;
import project.footprint.api.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @DisplayName("신규 사용자 가입")
    @Test
    void join() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";

        UserJoinRequest request = UserJoinRequest.builder()
                .email(email)
                .password(password)
                .passwordConfirm(password)
                .profileUrl(profileUrl)
                .nickname(nickname).build();

        userService.join(request);

        // when
        User findUser = userRepository.findByEmail(email);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser).extracting("nickname", "profileUrl")
                .contains(nickname, profileUrl);
    }

    @DisplayName("로그인 성공")
    @Test
    void login() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        User user = createUser(email, password, nickname, profileUrl);

        user.encryptPassword(passwordEncoder);

        userRepository.save(user);

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        // when
        String token = userService.login(userLoginRequest);

        // then
        assertThat(token).isNotNull();
    }

    private User createUser(String email, String password, String nickname, String profileUrl) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileUrl(profileUrl).build();
    }

    @DisplayName("로그인 실패 - 이메일 불일치")
    @Test
    void loginFailedByEmail() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        User user = createUser(email, password, nickname, profileUrl);

        user.encryptPassword(passwordEncoder);

        userRepository.save(user);

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("ggg@gmail.com")
                .password(password)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.login(userLoginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일이 일치하지 않습니다.");

    }

    @DisplayName("로그인 실패 - 패스워드 불일치")
    @Test
    void loginFailedByPassword() {
        // given
        String email = "jh13012@gmail.com";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        User user = createUser(email, password, nickname, profileUrl);

        user.encryptPassword(passwordEncoder);

        userRepository.save(user);

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email(email)
                .password("12345")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.login(userLoginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("패스워드가 일치하지 않습니다.");

    }
}