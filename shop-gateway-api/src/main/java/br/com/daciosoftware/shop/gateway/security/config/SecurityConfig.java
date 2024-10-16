package br.com.daciosoftware.shop.gateway.security.config;

import br.com.daciosoftware.shop.gateway.security.exception.CustomAccessDeniedHandler;
import br.com.daciosoftware.shop.gateway.security.exception.CustomAuthenticationEntryPoint;
import br.com.daciosoftware.shop.gateway.service.AuthService;
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
import java.util.Arrays;
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

        String SCOPE_PREFIX = "SCOPE_";
        final String[] ALL_SCOPES = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).toArray(String[]::new);
        final String SCOPE_ADMIN = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("admin")).findFirst().orElse("");
        final String SCOPE_OPERATOR = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("operator")).findFirst().orElse("");
        final String SCOPE_CUSTOMER = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("customer")).findFirst().orElse("");

        System.err.println("[#### Security Gateway Filter Chain ####] ");
        System.err.printf("All Scope %s%n", Arrays.toString(ALL_SCOPES));
        System.err.printf("Scope Admin %s%n", SCOPE_ADMIN);
        System.err.printf("Scope Operator %s%n", SCOPE_OPERATOR);
        System.err.printf("Scope Customer %s%n", SCOPE_CUSTOMER);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec

                        //Routes no authenticated - permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/customer/user").permitAll() //for create customer and user(auth)
                        .pathMatchers(HttpMethod.GET, "/gateway/healthcheck").permitAll()

                        //**** Routes authenticated ****
                        //Route authenticated for all scopes
                        .pathMatchers(HttpMethod.GET, "/auth/user/authenticated").hasAnyAuthority(ALL_SCOPES)

                        //Route for resources user (auth) - only rule SCOPE_Admin
                        .pathMatchers("/auth/user", "/auth/user/*", "/auth/user/**").hasAuthority(SCOPE_ADMIN)

                        //Route for resources customer
                        .pathMatchers("/customer", "/customer/*", "/customer/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        //Route for resources product
                        .pathMatchers(HttpMethod.GET, "/product/pageable").hasAnyAuthority(ALL_SCOPES)
                        .pathMatchers("/product", "/product/*", "/product/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)
                        .pathMatchers("/category", "/category/*").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        //Route for resources shopping
                        .pathMatchers(HttpMethod.POST, "/shopping").hasAnyAuthority(SCOPE_CUSTOMER)
                        .pathMatchers(HttpMethod.GET, "/shopping/my-shops").hasAnyAuthority(SCOPE_CUSTOMER)
                        .pathMatchers("/shopping", "/shopping/*", "/shopping/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        .anyExchange().authenticated())
                //.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler()).authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                //.addFilterBefore(new ExceptionHandlerFilter(), SecurityWebFiltersOrder.LAST)
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

