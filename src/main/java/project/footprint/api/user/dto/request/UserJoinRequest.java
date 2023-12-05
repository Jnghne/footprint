package project.footprint.api.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.footprint.api.user.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserJoinRequest {
    // TODO 형식 검증 , 테스트
    @NotNull
    @Email
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String passwordConfirm;
    @NotNull
    private String nickname;
    private String subEmail;  // 선택값
    private String profileUrl; // 선택값

    public static User toEntity(UserJoinRequest request) {
        return User.builder()
                .email(request.email)
                .password(request.password)
                .username(request.username)
                .nickname(request.nickname)
                .subEmail(request.subEmail)
                .profileUrl(request.profileUrl)
                .build();
    }


}
