package swp.todoapp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OAuth2Controller {

    @GetMapping("/oauth2/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User userPrinciple) {
        return userPrinciple.getAttributes();
    }

}
