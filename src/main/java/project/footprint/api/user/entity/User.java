package project.footprint.api.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.footprint.global.util.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@DynamicInsert
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String username;

    private String nickname;

    private String subEmail;

    private String provider;

    private String providerId;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private RoleType role;

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public static User of(String email, String password, String username, String nickname, String subEmail, String provider, String providerId, String profileUrl, RoleType role) {

        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .subEmail(subEmail)
                .provider(provider)
                .providerId(providerId)
                .profileUrl(profileUrl)
                .role(role)
                .build();
    }
}
