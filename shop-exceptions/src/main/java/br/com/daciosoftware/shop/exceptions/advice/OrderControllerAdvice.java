package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.order.OrderNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.order.controller"})
public class OrderControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorDTO handleUserNotFound(HttpServletRequest request, OrderNotFoundException shopNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Order - Venda não encontrado", request);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorDTO handleProductNotFound(HttpServletRequest request, ProductNotFoundException productNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Order - Produto não encontrado", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleInvalidKeyCustomer(HttpServletRequest request, CustomerInvalidKeyException invalidUserKeyException) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Order - Chave de autenticação inválida", request);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthUserNotFoundException.class)
    public ErrorDTO handleAuthUserNotFound(HttpServletRequest request, AuthUserNotFoundException authUserNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Order - Cliente não identificado", request);
    }

}
