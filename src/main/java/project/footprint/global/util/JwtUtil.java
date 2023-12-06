package project.footprint.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.footprint.api.user.oauth.dto.JwtInfo;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtInfo jwtInfo;

    public static String getHeader(String jwt) {
        return "Bearer" + " " + jwt;
    }

    public String createJwt(Long id, String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        return Jwts.builder()
                .setSubject("accessToken")
                .setClaims(claims)
                .claim("id", id)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtInfo.getExpirationTime()))
                .signWith(jwtInfo.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    /**
//     * 사용자 정보를 가지고 AccessToken, RefreshToken 생성
//     */
//    public TokenInfoDto createToken(Authentication authentication) {
//        // 사용자 권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        long now = (new Date()).getTime();
//
//        // Access Token
//        String accessToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .setExpiration(new Date(now + jwtInfo.getExpirationTime()))
//                .signWith(jwtInfo.getKey(), SignatureAlgorithm.HS256)
//                .compact();
//
//        // Refresh Token
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now + jwtInfo.getExpirationTime()))
//                .signWith(jwtInfo.getKey(), SignatureAlgorithm.HS256)
//                .compact();
//
//        return TokenInfoDto.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

    /**
     * JWT 토큰 정보 조회
     * - 토큰을 복호화해서, 토크 내부 정보를 꺼낸다.
     */
//    public Authentication getAuthentication(String accessToken) {
//        Claims claims = parseClaims(accessToken);
//        Object auth = claims.get("auth");
//
//        if (auth == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(auth.toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }

//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(jwtInfo.getKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (SecurityException | MalformedJwtException e){
//            log.warn("Invalid JWT Token : {}.", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            log.warn("Expired JWT Token : {}.", e.getMessage());
//        } catch (IllegalArgumentException e){
//            log.warn("JWT claims string is empty : {}.", e.getMessage());
//        }
//        return false;
//    }
//
//    private Claims parseClaims(String accessToken) {
//        return Jwts.parserBuilder()
//                .setSigningKey(jwtInfo.getKey())
//                .build()
//                .parseClaimsJws(accessToken)
//                .getBody();
//    }

}
