package br.com.daciosoftware.shop.gateway.security.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;


@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {


    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer configure) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configure.getWriters());
    }

    private static ErrorDTO getErrorDTO(Throwable throwable, ServerRequest request) {
        ErrorDTO errorDTO;
        if (throwable instanceof ExpiredJwtException) {
            errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Token expirado. Refaça o login", request);
        } else if (throwable instanceof SignatureException) {
            errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Erro na assinatura do token", request);
        } else if (throwable instanceof UnsupportedJwtException
                || throwable instanceof MalformedJwtException
                || throwable instanceof IllegalArgumentException) {
            errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Token inválido", request);
        } else {
            errorDTO = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor", request);
        }
        return errorDTO;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);
        ErrorDTO errorDTO = getErrorDTO(throwable, request);
        return ServerResponse.status(errorDTO.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorDTO.toString()));

    }

}