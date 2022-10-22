package nextstep.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class AuthController {

    @PostMapping("/login/token")
    TokenResponse createToken(@RequestBody TokenRequest tokenRequest) {
        return new TokenResponse("token");
    }
}
