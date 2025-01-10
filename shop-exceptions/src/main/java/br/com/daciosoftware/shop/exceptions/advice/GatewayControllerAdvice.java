package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {
        "br.com.daciosoftware.shop.customer.controller",
        "br.com.daciosoftware.shop.product.controller",
        "br.com.daciosoftware.shop.order.controller",
        "br.com.daciosoftware.shop.auth.controller"})
public class GatewayControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceAuthUnavailableException.class)
    public ErrorDTO handleServiceAuthUnavailableException(ServiceAuthUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço auth indisponível");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceCustomerUnavailableException.class)
    public ErrorDTO handleServiceCustomerUnavailableException(ServiceCustomerUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço customer indisponível");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceProductUnavailableException.class)
    public ErrorDTO handleServiceProductUnavailableException(ServiceProductUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço product indisponível");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceCategoryUnavailableException.class)
    public ErrorDTO handleServiceCategoryUnavailableException(ServiceCategoryUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço category indisponível");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthUnauthorizedException.class)
    public ErrorDTO handleAuthUnauthorizedException(AuthUnauthorizedException exception) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Recurso não autorizado");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthForbiddenException.class)
    public ErrorDTO handleAuthForbiddenException(AuthForbiddenException exception) {
        return new ErrorDTO(HttpStatus.FORBIDDEN.value(), "Acesso ao recurso negado");
    }

}
