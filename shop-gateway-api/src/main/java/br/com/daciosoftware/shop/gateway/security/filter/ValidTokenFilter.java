package br.com.daciosoftware.shop.gateway.security.filter;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.gateway.security.config.SecurityConfig;
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
import java.util.Arrays;

@Component
public class ValidTokenFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(ValidTokenFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String pathRequest = exchange.getRequest().getPath().toString();

        boolean contains = Arrays.asList(SecurityConfig.END_POINTS_WITHOUT_TOKEN).contains(pathRequest);

        System.out.printf("End Point %s%n", pathRequest);

        if (contains) return chain.filter(exchange);

        System.out.printf("End Point %s valida o token%n", pathRequest);


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
                throw ex;
            } catch (ExpiredJwtException ex) {
                //throw new AuthExpiredTokenException();
                log.error(ex.getMessage());
                throw ex;
            } catch (UnsupportedJwtException ex) {
                // Unsupported JWT token
                log.error(ex.getMessage());
                throw ex;
            } catch (MalformedJwtException ex) {
                // Malformed JWT token
                log.error(ex.getMessage());
                throw ex;
            } catch (IllegalArgumentException ex) {
                // JWT token is empty
                log.error(ex.getMessage());
                throw ex;
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return chain.filter(exchange);
    }
}