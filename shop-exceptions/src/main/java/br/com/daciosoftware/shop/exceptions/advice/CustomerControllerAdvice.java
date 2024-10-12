package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.customer.controller"})
public class CustomerControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ErrorDTO handleCustomerNotFound(CustomerNotFoundException userNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Cliente não encontrado");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorDTO handleCategoryNotFount(CategoryNotFoundException categoryNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Categoria dos interesses não encontrada");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerCpfExistsException.class)
    public ErrorDTO handleCustomerCpfExistsException(CustomerCpfExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "CPF já existe");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerEmailExistsException.class)
    public ErrorDTO handleCustomerEmailExistsException(CustomerEmailExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthPasswordNotMatchException.class)
    public ErrorDTO handlePasswordNotMatchException(AuthPasswordNotMatchException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Password do cliente não correspondem");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleCustomerInvalidKeyAuthException(CustomerInvalidKeyException ex) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Chave de autenticação inválida");
    }

}
