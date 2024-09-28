package br.com.daciosoftware.shop.security.config;

import br.com.daciosoftware.shop.security.RsaKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        System.err.println("**** Security Filter Chain **** ");

        http.authorizeExchange(authorize -> authorize
                        .pathMatchers(HttpMethod.GET, "/product").permitAll()
                        .pathMatchers(HttpMethod.GET, "/category").permitAll()
                        .anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        System.err.println("**** Security Jwt Encoder **** ");
        RsaKey rsaKey = new RsaKey();
        JWK jwk = new RSAKey.Builder(rsaKey.getPublicKey()).privateKey(rsaKey.getPrivate()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        System.err.println("**** Security Jwt Decoder **** ");
        RsaKey rsaKey = new RsaKey();
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKey.getPublicKey()).build();
    }


}
