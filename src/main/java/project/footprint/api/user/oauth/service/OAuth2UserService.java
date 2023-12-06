package project.footprint.api.user.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.footprint.api.user.oauth.domain.UserPrincipal;
import project.footprint.api.user.oauth.dto.OAuthAttributes;
import project.footprint.api.user.oauth.dto.ProviderType;
import project.footprint.api.user.entity.User;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.util.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase(); // google


        ProviderType providerType = ProviderType.valueOf(provider);

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(providerType, oAuth2User);
        Map<String,Object> attributes = oAuthAttributes.toMap();

        User savedUser = userRepository.findByEmail(oAuthAttributes.getEmail())
                .orElseGet(()->{
                    // 최초 가입 처리
                    User user = oAuthAttributes.toEntity();
                    user.encryptPassword(passwordEncoder);
                    return userRepository.save(user);
                });

        return UserPrincipal.of(savedUser, attributes);
    }
}
