package swp.todoapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import swp.todoapp.repository.TokenRepository;
import swp.todoapp.service.JwtService;
import java.io.IOException;

@Component
@Getter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ConstantConfiguration constant;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if(request.getServletPath().contains("/api/auth") ) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader =  request.getHeader(constant.HTTP_HEADER_AUTHORIZATION);
        final String jwt, username;

        if(authHeader == null || !authHeader.startsWith(constant.HTTP_HEADER_AUTHORIZATION_BEARER)){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(constant.HTTP_HEADER_AUTHORIZATION_BEARER.length());
        username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            boolean isTokenValid = tokenRepository.findByToken(jwt).map(
                    token -> !token.isExpired() && !token.isRevoked()
            ).orElse(false);

            if(jwtService.isValidToken(jwt, userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
