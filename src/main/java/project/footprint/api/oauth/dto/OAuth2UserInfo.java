package project.footprint.api.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

import static project.footprint.api.oauth.dto.ProviderType.GOOGLE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserInfo {

    String email;
    String username;
    String profileUrl;
    String provider;
    String providerId;

    public static OAuth2UserInfo of(ProviderType providerType, OAuth2User oAuth2User) {
        return switch (providerType) {
            case GOOGLE -> ofGoogle(oAuth2User);
            case NAVER -> ofNaver(oAuth2User);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }

    public static OAuth2UserInfo ofGoogle(OAuth2User oAuth2User) {
        // 1. 회원 정보 가져오기
        return OAuth2UserInfo.builder()
                .email(oAuth2User.getAttribute("sub"))
                .username(oAuth2User.getAttribute("name"))
                .profileUrl(oAuth2User.getAttribute("picture"))
                .build();
    }

    public static OAuth2UserInfo ofNaver(OAuth2User oAuth2User) {
        return OAuth2UserInfo.builder()
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


}
