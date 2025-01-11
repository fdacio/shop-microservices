package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthPasswordNotMatchException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserUsernameExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.*;
import br.com.daciosoftware.shop.exceptions.exceptions.product.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
    public ErrorDTO handleCustomerNotFound(HttpServletRequest request, CustomerNotFoundException userNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Cliente não encontrado", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorDTO handleCategoryNotFount(HttpServletRequest request, CategoryNotFoundException categoryNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Categoria dos interesses não encontrada", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerCpfExistsException.class)
    public ErrorDTO handleCustomerCpfExistsException(HttpServletRequest request, CustomerCpfExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "CPF já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerEmailExistsException.class)
    public ErrorDTO handleCustomerEmailExistsException(HttpServletRequest request, CustomerEmailExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe", request);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUserUsernameExistsException.class)
    public ErrorDTO handleAuthUserUsernameExistsException(HttpServletRequest request, AuthUserUsernameExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Username já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthEmailExistsException.class)
    public ErrorDTO handleAuthUserEmailExistsException(HttpServletRequest request, AuthEmailExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthPasswordNotMatchException.class)
    public ErrorDTO handlePasswordNotMatchException(HttpServletRequest request, AuthPasswordNotMatchException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Password do cliente não correspondem", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleCustomerInvalidKeyAuthException(HttpServletRequest request, CustomerInvalidKeyException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Chave de autenticação inválida", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerAuthUserConflictException.class)
    public ErrorDTO handleCustomerAuthUserConflictException(HttpServletRequest request, CustomerAuthUserConflictException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Cliente já vinculado a um usuário", request);
    }


}
