package br.com.daciosoftware.shop.security.gateway.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.err.println("[* Security Gateway Filter Chain *] ");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(HttpMethod.GET, "/category").permitAll()
                        //.pathMatchers(HttpMethod.GET, "/product").permitAll()
                        //.pathMatchers(HttpMethod.GET, GET_ROLE_NAME).hasRole("Role_Name")
                        .anyExchange().authenticated())
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        System.err.println("[* Security Gateway Jwt Encoder *] ");
        RsaKey rsaKey = new RsaKey();
        JWK jwk = new RSAKey.Builder(rsaKey.getPublicKey()).privateKey(rsaKey.getPrivate()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        System.err.println("[* Security Gateway Jwt Decoder *] ");
        RsaKey rsaKey = new RsaKey();
        return NimbusJwtDecoder.withPublicKey(rsaKey.getPublicKey()).build();
    }


}
