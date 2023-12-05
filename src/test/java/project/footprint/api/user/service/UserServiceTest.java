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
import project.footprint.global.util.PasswordEncoder;

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
    PasswordEncoder passwordEncoder;

    @DisplayName("신규 사용자 가입 - 모든 값을 입력한 경우")
    @Test
    void joinSuccessByAllInput() {
        // given
        String email = "jh13012@gmail.com";
        String username = "이종현";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        String subEmail = "jh13012@naver.com";

        UserJoinRequest request = UserJoinRequest.builder()
                .email(email)
                .password(password)
                .passwordConfirm(password)
                .username(username)
                .subEmail(subEmail)
                .profileUrl(profileUrl)
                .nickname(nickname).build();

        userService.join(request);

        // when
        User findUser = userRepository.findByEmail(email);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser).extracting("email","username","nickname", "subEmail", "profileUrl")
                .contains(email,username, nickname, subEmail, profileUrl);
    }

    @DisplayName("신규 사용자 가입 - 필수값만 입력된 경우")
    @Test
    void joinSuccessByRequiredInput() {
        // given
        String email = "jh13012@gmail.com";
        String username = "이종현";
        String password = "1234";
        String nickname = "testNick";

        UserJoinRequest request = UserJoinRequest.builder()
                .email(email)
                .password(password)
                .passwordConfirm(password)
                .username(username)
                .nickname(nickname).build();

        userService.join(request);

        // when
        User findUser = userRepository.findByEmail(email);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser).extracting("email","username","nickname", "subEmail", "profileUrl")
                .contains(email,username, nickname, null, null);
    }

    @DisplayName("로그인 성공")
    @Test
    void login() {
        // given
        String email = "jh13012@gmail.com";
        String username = "이종현";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        String subEmail = "jh13012@naver.com";
        User user = createUser(email, password, username,nickname, profileUrl,subEmail);

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

    private User createUser(String email, String password, String username, String nickname, String profileUrl, String subEmail) {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .subEmail(subEmail)
                .profileUrl(profileUrl).build();
    }

    @DisplayName("로그인 실패 - 이메일 불일치")
    @Test
    void loginFailedByEmail() {
        // given
        String email = "jh13012@gmail.com";
        String username = "이종현";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        String subEmail = "jh13012@naver.com";
        User user = createUser(email, password, username,nickname, profileUrl,subEmail);

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
        String username = "이종현";
        String password = "1234";
        String nickname = "testNick";
        String profileUrl = "localhost";
        String subEmail = "jh13012@naver.com";
        User user = createUser(email, password, username,nickname, profileUrl,subEmail);

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