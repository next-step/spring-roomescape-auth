package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String reservation() {
        return "admin/reservation";
    }

    @PostMapping("/admin/reservation")
    @ResponseBody
    public ResponseEntity<String> create() {
        return ResponseEntity.ok("success");
    }

    @GetMapping("/admin/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String theme() {
        return "admin/theme";
    }
}
