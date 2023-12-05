package project.footprint.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("request = " + request);
        // 토큰 찾기

        // 토큰에서 memberId를 찾아서

        // JWT 토큰을 가져오기
        // 1. 만약 JWT 토큰이 없다면 doFilter로 다음 필터처리
        // 2. JWT 토큰이 만료되었다면,  -
        // 3.

//        CustomUserDetails userDetails = new CustomUserDetails(findUser);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);

    }
}
