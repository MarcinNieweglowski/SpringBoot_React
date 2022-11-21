package com.marcin.springboot_react.configuration;

import com.marcin.springboot_react.exception.UserNotFoundException;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.service.UserService;
import com.marcin.springboot_react.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    private static final String BEARER = "Bearer ";

    @Value("${jwt.http.request.header}")
    private String jwtHeader;

    private static final List<String> NO_CHECK = List.of("/auth/login", "/auth/create-account", "health");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("Path: '{}'", path);
        log.info("contains path? {}", NO_CHECK.contains(path));
        return NO_CHECK.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Authenticating the request for URL: '{}'", request.getRequestURL());

        String tokenFromRequest = request.getHeader(this.jwtHeader);

        if (tokenFromRequest != null && tokenFromRequest.startsWith(BEARER) && tokenFromRequest.length() > BEARER.length()) {
            try {
                validateBearerToken(request, tokenFromRequest);
            } catch (UserNotFoundException e) {
                log.error("User not found");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
        } else {
            log.error("Invalid or no security token found");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or no security token found");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void validateBearerToken(HttpServletRequest request, String tokenFromRequest) throws UserNotFoundException {
        String token = tokenFromRequest.substring(BEARER.length());
        log.info("Retrieved the token from the request header");
        String username = this.jwtUtils.getUsernameFromToken(token);
        log.info("Retrieved the username: '{}' from the token", username);
        User user = this.userService.findByUsername(username);

        if (this.jwtUtils.isTokenValid(token, user)) {
            log.info("Token is still valid, adding the user attribute to the request");
            request.setAttribute("user", user);
        }
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

}
