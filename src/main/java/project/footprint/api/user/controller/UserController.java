package project.footprint.api.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.footprint.api.user.oauth.domain.UserPrincipal;
import project.footprint.api.user.dto.request.UserJoinRequest;
import project.footprint.api.user.dto.request.UserLoginRequest;
import project.footprint.api.user.service.UserService;
import project.footprint.global.util.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request) {
        String jwt = userService.login(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, JwtUtil.getHeader(jwt)).build();
    }

    // todo persistence layer 테스트
    @PostMapping
    public ResponseEntity<Void> join(@RequestBody @Valid UserJoinRequest joinRequest) {
        userService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/login")
    public String testLogin(Authentication authentication) {
        System.out.println("UserController.testLogin");
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        System.out.println("userDetails = " + userDetails);
        return "세션 정보 확인하기";
    }
    @GetMapping("/test/oauth/login")
    public String testLogin2(Authentication authentication ,@AuthenticationPrincipal UserPrincipal userDetails) {

        System.out.println("UserController.testLogin");
        System.out.println("oAuth2User = " + userDetails);
        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/admins")
    public ResponseEntity<String> getAdminList() {
        return ResponseEntity.ok().body("<h1>ADMIN</h1>");
    }
}
