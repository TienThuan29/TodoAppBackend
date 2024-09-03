package swp.todoapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp.todoapp.dto.auth.AuthenticationRequest;
import swp.todoapp.dto.auth.AuthenticationResponse;
import swp.todoapp.dto.auth.RegisterRequest;
import swp.todoapp.exception.def.InvalidTokenException;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.exception.def.UsernamePasswordInvalidException;
import swp.todoapp.service.AuthenticationService;
import swp.todoapp.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) throws NotFoundException {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
            throws UsernamePasswordInvalidException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/authenticate-oauth2")
    public ResponseEntity<AuthenticationResponse> authenticateOAuth2(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.authenticateOAuth2(email));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws UsernamePasswordInvalidException, InvalidTokenException, IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }

    @GetMapping("/has-account")
    public Map<String, Boolean> hasAccount(@RequestParam String email) {
        Map<String, Boolean> json = new HashMap<>();
        json.put("hasAccount", userService.hasAccountByEmail(email));
        return json;
    }
}
