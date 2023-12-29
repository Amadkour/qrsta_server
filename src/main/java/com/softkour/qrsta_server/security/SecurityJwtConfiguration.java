package com.softkour.qrsta_server.security;

import com.softkour.qrsta_server.config.SecurityMetersService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityJwtConfiguration {

    // @Value("${security.authentication.jwt.base64-secret}")
    // private String jwtKey;

    // @Bean
    // public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
    // NimbusJwtDecoder jwtDecoder =
    // NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM)
    // .build();
    // return token -> {
    // try {
    // return jwtDecoder.decode(token);
    // } catch (Exception e) {
    // if (e.getMessage().contains("Invalid signature")) {
    // metersService.trackTokenInvalidSignature();
    // } else if (e.getMessage().contains("Jwt expired at")) {
    // metersService.trackTokenExpired();
    // } else if (e.getMessage().contains("Invalid JWT serialization")) {
    // metersService.trackTokenMalformed();
    // } else if (e.getMessage().contains("Invalid unsecured/JWS/JWE")) {
    // metersService.trackTokenMalformed();
    // }
    // throw e;
    // }
    // };
    // }

    // @Bean
    // public JwtEncoder jwtEncoder() {
    // return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    // }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {
    // JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new
    // JwtGrantedAuthoritiesConverter();
    // grantedAuthoritiesConverter.setAuthorityPrefix("");
    // grantedAuthoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_KEY);

    // JwtAuthenticationConverter jwtAuthenticationConverter = new
    // JwtAuthenticationConverter();
    // jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    // return jwtAuthenticationConverter;
    // }

    // private SecretKey getSecretKey() {
    // byte[] keyBytes = Base64.from(jwtKey).decode();
    // return new SecretKeySpec(keyBytes, 0, keyBytes.length,
    // JWT_ALGORITHM.getName());
    // }
}
