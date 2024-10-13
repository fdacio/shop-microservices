package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.ProductNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShoppingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.shopping.controller"})
public class ShoppingControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ShoppingNotFoundException.class)
    public ErrorDTO handleUserNotFound(ShoppingNotFoundException shopNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "SHOP - Venda não encontrado");

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorDTO handleProductNotFound(ProductNotFoundException productNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "SHOP - Produto não encontrado");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleInvalidKeyCustomer(CustomerInvalidKeyException invalidUserKeyException) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "SHOP - Chave do cliente inválida");

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthUserNotFoundException.class)
    public ErrorDTO handleAuthUserNotFound(AuthUserNotFoundException authUserNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "SHOP - Cliente não identificado");
    }

}
