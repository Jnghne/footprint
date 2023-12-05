package project.footprint.api.user.oauth.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.footprint.api.user.entity.RoleType;
import project.footprint.api.user.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

// security session 안의 Authentication 객체 안에 들어갈 User객체
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {
    private final String email;
    private final String password;
    private final String username;
    private final String nickname;
    private final String subEmail;
    private final String provider;
    private final String providerId;
    private final String profileUrl;
    private final RoleType role;
    private final Collection<GrantedAuthority> authorities;
    private Map<String,Object> attributes;

    public static UserPrincipal of(User user) {
        return new UserPrincipal(user.getEmail()
                ,user.getPassword()
                ,user.getUsername()
                ,user.getNickname()
                ,user.getSubEmail()
                ,user.getProvider()
                ,user.getProviderId()
                ,user.getProfileUrl()
                ,user.getRole()
                , Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getType()))
        );
    }
    public static UserPrincipal of(User user, Map<String,Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.of(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
