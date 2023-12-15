package project.footprint.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.footprint.api.user.oauth.dto.JwtInfo;
import project.footprint.api.user.oauth.service.OAuth2UserService;
import project.footprint.api.user.entity.RoleType;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.filter.JwtFilter;
import project.footprint.global.handler.CustomAuthenticationEntryPoint;
import project.footprint.global.handler.OAuth2FailureHandler;
import project.footprint.global.handler.OAuth2SuccessHandler;
import project.footprint.global.util.JwtUtil;
import project.footprint.global.util.PasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final UserRepository userRepository;
    private final JwtInfo jwtInfo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CorsConfigurationSource의 cors 설정을 사용
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // disable session
                .headers((headerConfig) -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(new JwtFilter(userRepository, jwtInfo), UsernamePasswordAuthenticationFilter.class)
                // 인증/인가 설정
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/users/admins")).hasRole(RoleType.ADMIN.getType())
                                .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/users")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/connect")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .anyRequest().authenticated()
                )
                // oauth 로그인 시작
                // - spring oauth client 가 대신 인증 요청, 액세스 토큰 요청 과정을 자동으로 수행한다.
                .oauth2Login(oauth2LoginConfig -> {
                    oauth2LoginConfig
                            // OAuth2.0 로그인 완료된 이후 사용자의 정보 후처리
                            .userInfoEndpoint(
                                    userInfo -> userInfo.userService(new OAuth2UserService(userRepository,passwordEncoder))
                            )
                            .successHandler(new OAuth2SuccessHandler(userRepository,passwordEncoder,jwtUtil))
                            .failureHandler(failureHandler);


                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .logout(logoutConfigurer -> {
                    logoutConfigurer.permitAll();
                    logoutConfigurer.logoutSuccessUrl("/");
                })
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

