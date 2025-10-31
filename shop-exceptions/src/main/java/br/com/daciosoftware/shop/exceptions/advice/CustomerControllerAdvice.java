package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthPasswordNotMatchException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserUsernameExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.*;
import br.com.daciosoftware.shop.exceptions.exceptions.product.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.customer.controller"})
public class CustomerControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(CustomerControllerAdvice.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ErrorDTO handleCustomerNotFound(HttpServletRequest request, CustomerNotFoundException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Cliente não encontrado", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorDTO handleCategoryNotFount(HttpServletRequest request, CategoryNotFoundException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Categoria dos interesses não encontrada", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerCpfExistsException.class)
    public ErrorDTO handleCustomerCpfExistsException(HttpServletRequest request, CustomerCpfExistsException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "CPF já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerEmailExistsException.class)
    public ErrorDTO handleCustomerEmailExistsException(HttpServletRequest request, CustomerEmailExistsException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe", request);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUserUsernameExistsException.class)
    public ErrorDTO handleAuthUserUsernameExistsException(HttpServletRequest request, AuthUserUsernameExistsException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Username já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthEmailExistsException.class)
    public ErrorDTO handleAuthUserEmailExistsException(HttpServletRequest request, AuthEmailExistsException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthPasswordNotMatchException.class)
    public ErrorDTO handlePasswordNotMatchException(HttpServletRequest request, AuthPasswordNotMatchException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Password do cliente não correspondem", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleCustomerInvalidKeyAuthException(HttpServletRequest request, CustomerInvalidKeyException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Chave de autenticação inválida", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerAuthUserConflictException.class)
    public ErrorDTO handleCustomerAuthUserConflictException(HttpServletRequest request, CustomerAuthUserConflictException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Cliente já vinculado a um usuário", request);
    }


}
