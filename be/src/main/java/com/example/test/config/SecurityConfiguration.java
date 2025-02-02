package com.example.test.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.example.test.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableMethodSecurity(securedEnabled = false)
public class SecurityConfiguration {

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/account",
            "/api/v1/resumes/by-user",
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/companies/follow",
            "/api/v1/files/upload",
            "/api/v1/auth/logout",
            "/api/v1/companies/unfollow",
            "/api/v1/auth/refresh",
            "/api/v1/otps",
            "/api/v1/users/password/forgot-password",
            "/api/v1/resumes/by-job",
            "/api/v1/jobs",
            "/api/v1/jobs/get-one/**",
            "/api/v1/companies",
            "/api/v1/skills",
            "/api/v1/companies/get-one/**",
            "/api/v1/users/password/reset-password",
            "/api/v1/chats/**",
            "/actuator/**",
    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults())
                .formLogin((f) -> f.disable())
                .authorizeHttpRequests(
                        (authz) ->

                        authz

                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().permitAll()

                )
                // handle exceptions jwt
                .exceptionHandling((ex) -> ex
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    protected JwtEncoder accessTokenEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(JwtService.getSecretKey(jwtSecret)));
    }

    @Bean
    protected JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(JwtService.getSecretKey(jwtSecret))
                .macAlgorithm(JwtService.MAC_ALGORITHM).build();

        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                throw e;
            }
        };

    }

    @Bean
    protected JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
