package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.*;
import jakarta.servlet.http.HttpServletRequest;
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
    public ErrorDTO handleServiceAuthUnavailableException(HttpServletRequest request, ServiceAuthUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço auth indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceCustomerUnavailableException.class)
    public ErrorDTO handleServiceCustomerUnavailableException(HttpServletRequest request, ServiceCustomerUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço customer indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceProductUnavailableException.class)
    public ErrorDTO handleServiceProductUnavailableException(HttpServletRequest request, ServiceProductUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço product indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceCategoryUnavailableException.class)
    public ErrorDTO handleServiceCategoryUnavailableException(HttpServletRequest request, ServiceCategoryUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço category indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthUnauthorizedException.class)
    public ErrorDTO handleAuthUnauthorizedException(HttpServletRequest request, AuthUnauthorizedException exception) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Recurso não autorizado", request);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthForbiddenException.class)
    public ErrorDTO handleAuthForbiddenException(HttpServletRequest request, AuthForbiddenException exception) {
        return new ErrorDTO(HttpStatus.FORBIDDEN.value(), "Acesso ao recurso negado", request);
    }

}
