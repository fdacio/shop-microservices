package br.com.daciosoftware.shop.gateway.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.NoRouteToHostException;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.gateway.security.filter"})
public class GatewayExceptionAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthUnauthorizedException.class)
    public ErrorDTO handleAuthUnAuthorizedExceptionError(AuthUnauthorizedException ex) {
        return  new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Recurso não autorizado");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthForbiddenException.class)
    public ErrorDTO handleAuthForbiddenExceptionError(AuthForbiddenException ex) {
        return  new ErrorDTO(HttpStatus.FORBIDDEN.value(), "Recurso não autorizado");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthExpiredTokenException.class)
    public ErrorDTO handleAuthExpiredTokenExceptionError(AuthExpiredTokenException ex) {
        return  new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Token expirado. Refaça o login");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(NoRouteToHostException.class)
    public ErrorDTO handleAuthForbiddenExceptionError(NoRouteToHostException ex) {
        return  new ErrorDTO(HttpStatus.BAD_GATEWAY.value(), "Microsserviço indisponível");
    }

}
