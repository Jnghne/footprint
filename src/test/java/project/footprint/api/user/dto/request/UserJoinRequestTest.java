package project.footprint.api.user.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.footprint.api.user.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

class UserJoinRequestTest {

    @DisplayName("userJoinRequest dto를 통해 User entity를 생성할 수 있다.")
    @Test
    void toEntity() {
        // given
        String email = "jh13012@gmail.com";
        String password = "password123@";
        String nickname = "testNick";
        UserJoinRequest request = UserJoinRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        // when
        User user = UserJoinRequest.toEntity(request);

        // then
        assertThat(user).isNotNull();
        assertThat(user).extracting("email","password","nickname")
                .contains(email,password,nickname);
    }
}