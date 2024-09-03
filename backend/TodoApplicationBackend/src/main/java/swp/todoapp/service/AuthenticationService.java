package swp.todoapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swp.todoapp.config.ConstantConfiguration;
import swp.todoapp.config.MessageConfiguration;
import swp.todoapp.dto.auth.AuthenticationRequest;
import swp.todoapp.dto.auth.AuthenticationResponse;
import swp.todoapp.dto.auth.RegisterRequest;
import swp.todoapp.dto.info.UserDTO;
import swp.todoapp.exception.def.InvalidTokenException;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.exception.def.UsernamePasswordInvalidException;
import swp.todoapp.mapper.UserMapper;
import swp.todoapp.model.Token;
import swp.todoapp.model.TokenType;
import swp.todoapp.model.User;
import swp.todoapp.repository.RoleRepository;
import swp.todoapp.repository.TokenRepository;
import swp.todoapp.repository.UserRepository;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final ConstantConfiguration constant;

    private final MessageConfiguration messageConfig;


    private void saveToken(User user, String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(constant.JWT_EXPIRED_DISABLE)
                .revoked(constant.JWT_REVOKED_DISABLE)
                .user(user)
                .build();
        tokenRepository.save(token);
    }


    private void revokeAllOldUserToken(User user) {
        List<Token> validTokenList = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validTokenList.isEmpty()) {
            validTokenList.forEach(token -> {
//                token.setExpired(constant.JWT_EXPIRED_ENABLE);
//                token.setRevoked(constant.JWT_REVOKED_ENABLE);
                tokenRepository.deleteById(token.getId());
            });
//            tokenRepository.saveAll(validTokenList);
        }
    }


    public AuthenticationResponse register(RegisterRequest registerRequest) throws NotFoundException {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .fullname(registerRequest.getFullname())
                .email(registerRequest.getEmail())
                .enable(registerRequest.isEnable())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(
                        roleRepository.findByCode(registerRequest.getRoleCode()).orElseThrow(
                                () -> new NotFoundException(
                                        messageConfig.ERROR_ROLE_NOTFOUND+registerRequest.getRoleCode()
                                )
                        )
                )
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authRequest)
            throws UsernamePasswordInvalidException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword()
            ));
        }
        catch (AuthenticationException exception) {
            throw new UsernamePasswordInvalidException(messageConfig.ERROR_USERNAME_PASSWORD_INVALID);
        }

        User user = getUser(authRequest.getUsername());

        String jwtToken = jwtService.generateToken(user); // Access token
        String refreshToken = jwtService.generateRefreshToken(user);  // Refresh token

        revokeAllOldUserToken(user);
        saveToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticateOAuth2(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllOldUserToken(user);
        saveToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws InvalidTokenException, IOException {
        final String authHeader = request.getHeader(constant.HTTP_HEADER_AUTHORIZATION);
        if (authHeader == null ||!authHeader.startsWith(constant.HTTP_HEADER_AUTHORIZATION_BEARER))
            return new AuthenticationResponse(null, null);

        final String refreshToken =authHeader.substring(constant.HTTP_HEADER_AUTHORIZATION_BEARER.length());
        final String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            User user = getUser(username);
            if(jwtService.isValidToken(refreshToken, user)) {
                String newAccessToken = jwtService.generateToken(user);
                revokeAllOldUserToken(user);
                saveToken(user, newAccessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return authResponse;
            }
            else throw new InvalidTokenException(messageConfig.ERROR_JWT_INVALID_TOKEN);
        }
        return new AuthenticationResponse(null, null);
    }


    public UserDTO getUserInfo(HttpServletRequest request, HttpServletResponse response) throws InvalidTokenException {
        final String authHeader = request.getHeader(constant.HTTP_HEADER_AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(constant.HTTP_HEADER_AUTHORIZATION_BEARER))
            throw new InvalidTokenException(messageConfig.ERROR_JWT_INVALID_TOKEN);

        String jwt = authHeader.substring(constant.HTTP_HEADER_AUTHORIZATION_BEARER.length());
        String username = jwtService.extractUsername(jwt);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return UserMapper.toUserDTO(user);
    }


    private User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernamePasswordInvalidException(messageConfig.ERROR_USERNAME_PASSWORD_INVALID)
        );
    }

}
