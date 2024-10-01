package br.com.daciosoftware.shop.security.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.err.println("[* Security Gateway Filter Chain *] ");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                //.exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        //.pathMatchers(HttpMethod.GET, "/user").permitAll()
                        //.pathMatchers(HttpMethod.GET, GET_ROLE_NAME).hasRole("Role_Name")
                        .anyExchange().authenticated())
                //.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        System.err.println("[* Security Gateway Jwt Decoder *] ");
        RsaKey rsaKey = new RsaKey();
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKey.getPublicKey()).build();
    }
}
