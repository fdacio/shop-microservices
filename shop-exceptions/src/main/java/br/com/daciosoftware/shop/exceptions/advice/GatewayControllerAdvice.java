package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(GatewayControllerAdvice.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceAuthUnavailableException.class)
    public ErrorDTO handleServiceAuthUnavailableException(HttpServletRequest request, MicroserviceAuthUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Microservice Auth indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceCustomerUnavailableException.class)
    public ErrorDTO handleServiceCustomerUnavailableException(HttpServletRequest request, MicroserviceCustomerUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Microservice Customer indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceProductUnavailableException.class)
    public ErrorDTO handleServiceProductUnavailableException(HttpServletRequest request, MicroserviceProductUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Microservice Product indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceCategoryUnavailableException.class)
    public ErrorDTO handleServiceCategoryUnavailableException(HttpServletRequest request, MicroserviceCategoryUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Microservice Category indisponível", request);
    }

}
