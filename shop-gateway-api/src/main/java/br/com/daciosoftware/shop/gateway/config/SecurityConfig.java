package br.com.daciosoftware.shop.gateway.config;

import br.com.daciosoftware.shop.gateway.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthService authService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.err.println("[### Security Gateway Filter Chain ###] ");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(HttpMethod.POST, "/customer/user").permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.GET, "/gateway/healthcheck").permitAll()
                        .pathMatchers(HttpMethod.GET, "/auth/user/authenticated").hasAnyAuthority("SCOPE_Admin", "SCOPE_Basic")
                        .pathMatchers(HttpMethod.GET, "/auth/user").hasAuthority("SCOPE_Admin")
                        .pathMatchers(HttpMethod.GET, "/auth/user/*").hasAuthority("SCOPE_Admin")
                        .anyExchange().authenticated())
                //.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        try {
            String publicKey = authService.getContentPublicKey();
            byte[] encoded = Base64.getDecoder().decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return NimbusReactiveJwtDecoder.withPublicKey(rsaPublicKey).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

