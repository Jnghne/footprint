package project.footprint.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/login")
    public String login() {
        return "test";
    }
    @GetMapping("/admin")
    public String admin() {
        return "test2";
    }
}
