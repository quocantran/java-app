package com.example.test.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.User;
import com.example.test.domain.request.RequestLoginDTO;
import com.example.test.domain.response.ResponseLoginDTO;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtService jwtService;

    public AuthService(UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder,
            JwtService jwtService) {
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtService = jwtService;
    }

    public ResponseLoginDTO login(RequestLoginDTO loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User current = this.userService.getUserByEmail(loginDto.getEmail());

            String accessToken = this.jwtService.createAccessToken(current.getEmail(), current);

            ResponseLoginDTO response = new ResponseLoginDTO(accessToken, current.convertResponseUserDto());

            return response;
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
