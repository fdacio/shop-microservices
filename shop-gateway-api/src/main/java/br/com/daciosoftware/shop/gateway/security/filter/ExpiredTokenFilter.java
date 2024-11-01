package br.com.daciosoftware.shop.gateway.security.filter;

import br.com.daciosoftware.shop.gateway.security.exception.AuthExpiredTokenException;
import br.com.daciosoftware.shop.gateway.security.service.AuthService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class ExpiredTokenFilter implements WebFilter {

    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorization != null) {

            String token = authorization.replace("Bearer ", "");

            AuthService authService = new AuthService();
            String privateKey = authService.getContentPublicKey();

            try {
                Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
            } catch (SignatureException ex) {
                // Invalid signature/claims
            } catch (ExpiredJwtException ex) {
                throw new AuthExpiredTokenException();
            } catch (UnsupportedJwtException ex) {
                // Unsupported JWT token
            } catch (MalformedJwtException ex) {
                // Malformed JWT token
            } catch (IllegalArgumentException ex) {
                // JWT token is empty
            }
        }

        return chain.filter(exchange);
    }
}