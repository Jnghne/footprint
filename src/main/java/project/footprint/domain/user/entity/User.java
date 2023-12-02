package project.footprint.domain.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;
    private String socialProvider;
    private String profileUrl;
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
