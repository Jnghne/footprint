package project.footprint.api.user.oauth.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.expression.spel.ast.OpGE;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.footprint.api.user.entity.RoleType;
import project.footprint.api.user.entity.User;

import java.util.HashMap;
import java.util.Map;

import static project.footprint.api.user.oauth.dto.ProviderType.GOOGLE;
import static project.footprint.api.user.oauth.dto.ProviderType.NAVER;

@Data
@Builder
public class OAuthAttributes {
    String email;
    String username;
    String profileUrl;
    String nickname;
    String provider;
    String providerId;

    public static OAuthAttributes of(ProviderType providerType, OAuth2User oAuth2User) {
        return switch (providerType) {
            case GOOGLE -> ofGoogle(oAuth2User);
            case NAVER -> ofNaver(oAuth2User);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
    public static OAuthAttributes ofGoogle(OAuth2User oAuth2User) {
        // 1. 회원 정보 가져오기
        return OAuthAttributes.builder()
                .email(oAuth2User.getAttribute("email"))
                .username(oAuth2User.getAttribute("name"))
                .provider(GOOGLE.getType())
                .providerId(oAuth2User.getAttribute("sub"))
                .profileUrl(oAuth2User.getAttribute("picture"))
                .build();
    }

    public static OAuthAttributes ofNaver(OAuth2User oAuth2User) {
        Map<String, String> response = (Map<String, String>) oAuth2User.getAttributes().get("response");
        return OAuthAttributes.builder()
                .email(response.get("email"))
                .username(response.get("name"))
                .nickname(response.get("nickname"))
                .provider(NAVER.getType())
                .providerId(response.get("id"))
                .profileUrl(response.get("profile_image"))
                .build();
    }
    public Map<String,Object> toMap() {
        Map<String, Object> oauth2UserMap = new HashMap<>();
        oauth2UserMap.put("email", email);
        oauth2UserMap.put("username", username);
        oauth2UserMap.put("profileUrl", profileUrl);
        oauth2UserMap.put("provider", provider);
        oauth2UserMap.put("providerId", providerId);
        return oauth2UserMap;
    }

    public User toEntity() {
        return User.of(this.getEmail(),
                "footprint",
                this.getUsername(),
                this.getUsername(),
                null,
                this.getProvider(),
                this.getProviderId(),
                this.getProfileUrl(),
                RoleType.USER);
    }
}
