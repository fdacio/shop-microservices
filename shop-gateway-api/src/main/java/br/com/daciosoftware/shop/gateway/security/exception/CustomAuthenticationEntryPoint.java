package br.com.daciosoftware.shop.gateway.security.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    //Authentication entry point has commence method when failures occur
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        throw new AuthUnauthorizedException();
        //        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Recurso não autorizado");
//        ServerHttpResponse response = exchange.getResponse();
//        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        String responseBody = errorDTO.toString();
//        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
//        DataBuffer buffer = response.bufferFactory().wrap(bytes);
//        return response.writeWith(Mono.just(buffer));
    }
}
