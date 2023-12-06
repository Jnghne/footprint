package project.footprint.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import project.footprint.api.user.entity.User;
import project.footprint.api.user.oauth.domain.UserPrincipal;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.util.JwtUtil;
import project.footprint.global.util.PasswordEncoder;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // todo OAuth 인증 간 사용 된 쿠키 clear
        // JWT 토큰 생성
        User findUser = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new RuntimeException(" 멤버가 없음 "));
        String jwt = jwtUtil.createJwt(findUser.getId(), findUser.getEmail());

        // 토큰 리턴
        // 1. 엑세스 토큰 : 헤더에 저장
        response.setHeader(HttpHeaders.AUTHORIZATION, JwtUtil.getHeader(jwt));

        // 2. 리프레쉬 토큰 : 쿠키에 저장 (오래 보관되기 때문에)
        Cookie cookie = new Cookie("refreshToken",  "refreshToken");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setStatus(SC_OK);

//        // 3. redirect
//        String url = UriComponentsBuilder
//                .fromUriString("http://localhost:3000/oauth/success")
//                .queryParam("token",":")
//                .build().toUriString();
//
//        getRedirectStrategy().sendRedirect(request, response, url);
    }


}
