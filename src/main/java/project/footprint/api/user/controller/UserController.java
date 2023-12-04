package project.footprint.api.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.footprint.api.user.dto.request.UserJoinRequest;
import project.footprint.api.user.dto.request.UserLoginRequest;
import project.footprint.api.user.service.UserService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok().headers(h->h.set("Authorization",token)).build();
    }

    // todo persistence layer 테스트
    @PostMapping
    public ResponseEntity<Void> join(@RequestBody @Valid UserJoinRequest joinRequest) {
        userService.join(joinRequest);
        return ResponseEntity.ok().headers(h -> h.setLocation(URI.create("/users/login"))).build();
    }

    @GetMapping("/admins")
    public ResponseEntity<String> getAdminList() {
        return ResponseEntity.ok().body("<h1>ADMIN</h1>");
    }
}
