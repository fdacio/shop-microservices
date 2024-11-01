package br.com.daciosoftware.shop.gateway.security.filter;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.gateway.security.exception.AuthExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class ValidTokenFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(ValidTokenFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorization != null) {

            String token = authorization.replace("Bearer ", "");

            try {
                Jwts.parser()
                        .setSigningKey(new RsaKey().getRsaPrivateKey())
                        .parseClaimsJws(token);
            } catch (SignatureException ex) {
                log.error(ex.getMessage());
                // Invalid signature/claims
            } catch (ExpiredJwtException ex) {
                throw new AuthExpiredTokenException();
            } catch (UnsupportedJwtException ex) {
                log.error(ex.getMessage());
                // Unsupported JWT token
            } catch (MalformedJwtException ex) {
                log.error(ex.getMessage());
                // Malformed JWT token
            } catch (IllegalArgumentException ex) {
                log.error(ex.getMessage());
                // JWT token is empty
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return chain.filter(exchange);
    }
}