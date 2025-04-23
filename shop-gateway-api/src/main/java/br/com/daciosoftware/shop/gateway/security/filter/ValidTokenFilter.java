package br.com.daciosoftware.shop.gateway.security.filter;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.gateway.security.exception.ErrorDTO;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static br.com.daciosoftware.shop.gateway.security.config.SecurityConfig.PUBLIC_END_POINTS;

@Component
public class ValidTokenFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(ValidTokenFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        AntPathMatcher pathMatcher = new AntPathMatcher();
        String pathRequest = exchange.getRequest().getPath().toString();

        for (String pattern : PUBLIC_END_POINTS) {
            if (pathMatcher.match(pattern, pathRequest)) {
                return chain.filter(exchange);
            }
        }

        log.warn("Filter Valid Token");

        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorization != null) {

            String token = authorization.replace("Bearer ", "");

            try {
                Jwts.parser().setSigningKey(new RsaKey().getRsaPrivateKey()).parseClaimsJws(token);
            } catch (SignatureException ex) {
                log.error(ex.getMessage());
                return onError(exchange, "Assinatura inválida");
            } catch (ExpiredJwtException ex) {
                log.error(ex.getMessage());
                return onError(exchange, "Token expirado");
            } catch (UnsupportedJwtException | MalformedJwtException | NoSuchAlgorithmException |
                     InvalidKeySpecException | IllegalArgumentException ex) {
                log.error(ex.getMessage());
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

}
