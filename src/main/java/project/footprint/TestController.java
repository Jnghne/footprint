package project.footprint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String Index() {
        return "<H1>인덱스 페이지</H1>";
    }

    @GetMapping("/connect")
    public String connectTest() {
        return "프론트-백엔드 연동 성공 (get)";
    }

    @PostMapping("/connect")
    public String connectTest2(@RequestBody Object param) {
        return "프론트-백엔드 연동 성공 (post)";
    }
}
