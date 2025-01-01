package br.com.daciosoftware.shop.gateway.security.config;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.gateway.security.exception.CustomAccessDeniedHandler;
import br.com.daciosoftware.shop.gateway.security.exception.CustomAuthenticationEntryPoint;
import br.com.daciosoftware.shop.gateway.security.filter.ValidTokenFilter;
import br.com.daciosoftware.shop.gateway.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
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
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private AuthService authService;
    @Autowired
    private RsaKey rsaKey;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.err.println("**** Setting SecurityWebFilterChain **** ");
        String SCOPE_PREFIX = "SCOPE_";
        final String[] ALL_SCOPES = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).toArray(String[]::new);
        final String SCOPE_ADMIN = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("admin")).findFirst().orElse("");
        final String SCOPE_OPERATOR = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("operator")).findFirst().orElse("");
        final String SCOPE_CUSTOMER = Arrays.stream(authService.getRules()).map(SCOPE_PREFIX::concat).filter(rule -> rule.toLowerCase().contains("customer")).findFirst().orElse("");

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // configurações para teste de localhost:3000
                //.cors(ServerHttpSecurity.CorsSpec::disable) // descomentar para ambiente de produção, quando o frontend no mesmo servidor do backend
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        //Routes no authenticated - permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/refresh-token").permitAll()
                        .pathMatchers(HttpMethod.POST, "/customer/user").permitAll() //for create customer and user(auth)
                        .pathMatchers(HttpMethod.GET, "/product/all/home", "/product/all/home/*").permitAll()
                        .pathMatchers(HttpMethod.GET, "/gateway/healthcheck").permitAll()

                        //**** Routes authenticated ****
                        //Route authenticated for all scopes
                        .pathMatchers(HttpMethod.POST, "/auth/user/authenticated").hasAnyAuthority(ALL_SCOPES)

                        //Route for resources user (auth) - only rule SCOPE_Admin
                        .pathMatchers("/auth/user/**").hasAuthority(SCOPE_ADMIN)
                        .pathMatchers( "/auth/config/**").hasAuthority(SCOPE_ADMIN)

                        //Route for resources customer
                        .pathMatchers("/customer/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        //Route for resources product
                        .pathMatchers(HttpMethod.GET, "/product/pageable").hasAnyAuthority(ALL_SCOPES)
                        .pathMatchers("/product/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)
                        .pathMatchers("/category/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)

                        //Route for resources orders
                        .pathMatchers(HttpMethod.POST, "/order").hasAuthority(SCOPE_CUSTOMER)
                        .pathMatchers(HttpMethod.GET, "/order/my-orders").hasAuthority(SCOPE_CUSTOMER)
                        .pathMatchers("/order/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)
                        .pathMatchers("/order/report/**").hasAnyAuthority(SCOPE_ADMIN, SCOPE_OPERATOR)
                        .anyExchange().authenticated())
                //.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .addFilterBefore(new ValidTokenFilter(), SecurityWebFiltersOrder.FIRST)
                .build();
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
    CorsConfigurationSource corsConfigurationSource() {
        System.err.println("**** Setting Config CORS **** ");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

