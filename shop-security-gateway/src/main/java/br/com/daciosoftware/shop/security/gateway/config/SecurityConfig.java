package br.com.daciosoftware.shop.security.gateway.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableMBeanExport
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.err.println("[* Security Gateway Filter Chain *] ");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.GET, "/auth/user/authenticated").hasAnyAuthority("SCOPE_Admin")
                        .pathMatchers(HttpMethod.GET, "/auth/user/authenticated").hasAnyAuthority("SCOPE_Basic")
                        .pathMatchers(HttpMethod.GET, "/auth/user").hasAuthority("SCOPE_Admin")
                        .pathMatchers(HttpMethod.GET, "/auth/user/*").hasAuthority("SCOPE_Admin")
                        .anyExchange().authenticated())
                //.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
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
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        System.err.println("[* Security Gateway Jwt Decoder *] ");
        RsaKey rsaKey = new RsaKey();
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKey.getPublicKey()).build();
    }
}
