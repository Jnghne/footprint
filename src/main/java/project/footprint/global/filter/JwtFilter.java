package project.footprint.global.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.footprint.api.user.entity.User;
import project.footprint.api.user.oauth.domain.UserPrincipal;
import project.footprint.api.user.oauth.dto.JwtInfo;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.util.JwtUtil;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtInfo jwtInfo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 1. header에 Jwt 토큰이 있는지 확인
        if(!StringUtils.hasText(jwtHeader) || !jwtHeader.startsWith("Bearer")){
            log.warn("헤더에 토큰이 존재하지 않음");
            chain.doFilter(request,response);
            return;
        }

        // 2. JWT 토큰 검증
        // 2.1. 토큰 파싱
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

        //TODO 리팩토링 : JwtUtil로 복호화 기능 분리
        //TODO 기능추가 : 만료된 토큰에 대한 처리 (refresh token 도 적용??)

        // 복호화
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(jwtInfo.getKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (SignatureException e){
            throw new SignatureException("JWT 서명 정보가 맞지 않습니다.");
        }

        String email = String.valueOf(claims.get("email"));

        if(email != null) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(" 멤버가 없음 "));
            UserPrincipal userPrincipal = UserPrincipal.of(user);

            //JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만든다
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

            // 시큐리티 세션에 접근해서 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request,response);

    }
}
