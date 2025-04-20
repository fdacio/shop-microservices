package br.com.daciosoftware.shop.gateway.security.config;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.gateway.security.exception.CustomAccessDeniedHandler;
import br.com.daciosoftware.shop.gateway.security.exception.CustomAuthenticationEntryPoint;
import br.com.daciosoftware.shop.gateway.security.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity //reactive stack - Non Blocking Code
@EnableReactiveMethodSecurity()
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    public static final String[] PUBLIC_END_POINTS = {
            "/home",
            "/auth/login",
            "/auth/refresh-token",
            "/customer/user",
            "/product/all/home",
            "/product/all/home**",
            "/product/*/photo",
            "/gateway/healthcheck"
    };

    @Autowired
    private AuthService authService;
    @Autowired
    private RsaKey rsaKey;

    @Order(0)
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {

        final String SCOPE_PREFIX = "SCOPE_";
        final String[] ALL_SCOPES = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).toArray(String[]::new);
        final String SCOPE_ADMIN = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("admin")).findFirst().orElse("");
        final String SCOPE_OPERATOR = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("operator")).findFirst().orElse("");
        final String SCOPE_CUSTOMER = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("customer")).findFirst().orElse("");

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec

                        .pathMatchers(PUBLIC_END_POINTS).permitAll()

                        //**** Routes authenticated ****
                        //Route authenticated for all scopes
                        .pathMatchers(HttpMethod.POST, "/auth/user/authenticated").hasAnyAuthority(ALL_SCOPES)

                        //Route for resources user (auth) - only rule SCOPE_Admin
                        .pathMatchers("/auth/user/**").hasAuthority(SCOPE_ADMIN)
                        .pathMatchers("/auth/config/**").hasAuthority(SCOPE_ADMIN)

                        //Route for resources customer
                        .pathMatchers("/customer/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        //Route for resources product
                        .pathMatchers(HttpMethod.GET, "/product/pageable").hasAnyAuthority(ALL_SCOPES)
                        .pathMatchers("/product/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)
                        .pathMatchers("/category/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        //Route for resources orders
                        .pathMatchers(HttpMethod.POST, "/order").hasAuthority(SCOPE_CUSTOMER)
                        .pathMatchers(HttpMethod.GET, "/order/my-orders").hasAuthority(SCOPE_CUSTOMER)
                        .pathMatchers(HttpMethod.GET, "/order/*/my-order").hasAuthority(SCOPE_CUSTOMER)
                        .pathMatchers(HttpMethod.PATCH, "/order/*/my-order").hasAuthority(SCOPE_CUSTOMER)
                        .pathMatchers(HttpMethod.DELETE, "/order/*/my-order").hasAuthority(SCOPE_CUSTOMER)

                        .pathMatchers("/order/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)
                        .pathMatchers("/order/report/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        .anyExchange().authenticated()

                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .jwt(withDefaults()))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );


        return http.build();
    }


    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        try {
            String publicKey = rsaKey.getPublicKeyDTO().getContent();
            byte[] encoded = Base64.getDecoder().decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return NimbusReactiveJwtDecoder.withPublicKey(rsaPublicKey).build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
//
//    private ServerAuthenticationEntryPoint authenticationEntryPoint() {
//        return (exchange, ex) -> unauthorized(exchange);
//    }
//
//    private ServerAccessDeniedHandler accessDeniedHandler() {
//        return (exchange, denied) -> forbidden(exchange);
//    }
//
//    private Mono<Void> unauthorized(ServerWebExchange exchange) {
//        log.info("unauthorized method call");
//        String message = "Você precisa estar autenticado para acessar esse recurso.";
//        return writeError(exchange, HttpStatus.UNAUTHORIZED, message);
//
//    }
//
//    private Mono<Void> forbidden(ServerWebExchange exchange) {
//        String message = "Você não tem permissão para acessar esse recurso.";
//        return writeError(exchange, HttpStatus.FORBIDDEN, message);
//    }
//
//    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {
//        if (exchange.getResponse().isCommitted()) {
//            return Mono.empty();
//        }
//        log.info("writeError");
//        exchange.getResponse().setStatusCode(status);
//        exchange.getResponse().getHeaders().add("Content-Type", "application/json; charset=UTF-8");
//
//        String body = String.format("{\"status\":%d,\"error\":\"%s\"}", status.value(), message);
//        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
//
//        return exchange.getResponse()
//                .writeWith(Mono.fromSupplier(() ->
//                        exchange.getResponse().bufferFactory().wrap(bytes)
//                ));
//    }

}

