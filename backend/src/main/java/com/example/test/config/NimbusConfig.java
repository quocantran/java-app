package com.example.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;

@Configuration
public class NimbusConfig {

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Bean
    protected JWTProcessor<SecurityContext> jwtProcessor(Gson gson) {
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        return jwtProcessor;
    }

    @Bean
    protected MACSigner macSigner() throws JOSEException {
        return new MACSigner(jwtSecret);
    }

    @Bean
    protected MACVerifier macVerifier() throws JOSEException {
        return new MACVerifier(jwtSecret);
    }
}
