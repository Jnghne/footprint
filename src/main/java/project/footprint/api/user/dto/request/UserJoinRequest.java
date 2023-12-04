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
    // TODO
    //  - 테스트
    //  - 형식 검증
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String passwordConfirm;
    @NotNull
    private String nickname;
    private String profileUrl; // null 허용

    public static User toEntity(UserJoinRequest request) {
        return User.builder()
                .email(request.email)
                .password(request.password)
                .nickname(request.nickname)
                .profileUrl(request.profileUrl)
                .build();
    }


}
