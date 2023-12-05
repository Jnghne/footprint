package project.footprint.api.user.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.footprint.api.user.oauth.domain.UserPrincipal;
import project.footprint.api.user.oauth.dto.OAuth2UserInfo;
import project.footprint.api.user.oauth.dto.ProviderType;
import project.footprint.api.user.entity.RoleType;
import project.footprint.api.user.entity.User;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.util.PasswordEncoder;

import java.util.Map;

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

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(providerType, oAuth2User);

        User savedUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        Map<String,Object> attributes = oAuth2UserInfo.toMap();


        // 최초 로그인 시 , 가입 처리
        if(savedUser == null) {
            User user = User.of(oAuth2UserInfo.getEmail(),
                    "footprint",
                    oAuth2UserInfo.getUsername(),
                    oAuth2UserInfo.getUsername(),
                    null,
                    oAuth2UserInfo.getProvider(),
                    oAuth2UserInfo.getProviderId(),
                    oAuth2UserInfo.getProfileUrl(),
                    RoleType.USER);
            user.encryptPassword(passwordEncoder);
            savedUser = userRepository.save(user);
        }

        return UserPrincipal.of(savedUser, attributes);
    }
}
