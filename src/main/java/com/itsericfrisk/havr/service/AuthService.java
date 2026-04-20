package com.itsericfrisk.havr.service;

import com.itsericfrisk.havr.dto.LoginRequest;
import com.itsericfrisk.havr.dto.RegisterRequest;
import com.itsericfrisk.havr.model.Roles;
import com.itsericfrisk.havr.model.User;
import com.itsericfrisk.havr.repository.UserRepository;
import com.itsericfrisk.havr.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;

    /**
     * Register a new user
     *
     * @param registerRequest Required user data
     */
    public void register(RegisterRequest registerRequest) {
        User user = new User(registerRequest.email(), registerRequest.name(), encoder.encode(registerRequest.password()), Roles.USER);
        repository.save(user);
    }

    /**
     * Login user
     *
     * @param loginRequest Required user data
     * @return accessToken and its validity time
     */
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        List<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .map(auth -> auth.replace("ROLE_", "")).toList();
        User user = repository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return jwtUtils.generateAccessToken(user.getId(), roles);
    }

}
