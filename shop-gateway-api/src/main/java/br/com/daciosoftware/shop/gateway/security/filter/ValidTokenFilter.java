package br.com.daciosoftware.shop.gateway.security.filter;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.gateway.security.exception.ErrorDTO;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static br.com.daciosoftware.shop.gateway.security.config.SecurityConfig.END_POINTS_WITHOUT_TOKEN;

@Component
public class ValidTokenFilter implements GlobalFilter, Ordered {

    public ValidTokenFilter() {
        log.info("**** ValidaTokenFilter Constructor **** ");

    }

    private static final Logger log = LoggerFactory.getLogger(ValidTokenFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String pathRequest = exchange.getRequest().getPath().toString();
        boolean contains = Arrays.asList(END_POINTS_WITHOUT_TOKEN).contains(pathRequest);
        log.info("End Point: {}", pathRequest);

        //Se for um end point public no check token
        if (contains) return chain.filter(exchange);

        log.info("End Point: {} valida o token", pathRequest);

        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorization != null) {

            String token = authorization.replace("Bearer ", "");

            try {
                Jwts.parser()
                        .setSigningKey(new RsaKey().getRsaPrivateKey())
                        .parseClaimsJws(token);
            } catch (SignatureException ex) {
                // Invalid signature/claims
                log.error(ex.getMessage());
                return onError(exchange, "Assinatura inválida");
            } catch (ExpiredJwtException ex) {
                log.error(ex.getMessage());
                return onError(exchange, "Token expirado");
            } catch (UnsupportedJwtException ex) {
                // Unsupported JWT token
                return onError(exchange, "Token inválido");
            } catch (MalformedJwtException ex) {
                // Malformed JWT token
                log.error(ex.getMessage());
                return onError(exchange, "Token inválido");
            } catch (IllegalArgumentException ex) {
                // JWT token is empty
                log.error(ex.getMessage());
                return onError(exchange, "Token inválido");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                log.error(e.getMessage());
                return onError(exchange, "Token inválido");
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), message, exchange.getRequest());
        String responseBody = errorDTO.toString();
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }


    @Override
    public int getOrder() {
        return -1;
    }
}