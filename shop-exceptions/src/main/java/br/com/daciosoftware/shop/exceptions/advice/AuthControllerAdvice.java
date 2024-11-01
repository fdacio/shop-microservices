package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.*;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerCpfExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.auth.controller"})
public class AuthControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthUserNotFoundException.class)
    public ErrorDTO handleAuthUserNotFound(AuthUserNotFoundException authUserNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Usuário não encontrado");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthInvalidLoginException.class)
    public ErrorDTO handleAuthLoginInvalid(AuthInvalidLoginException authLoginErrorException) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Usuário ou senha inválido");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthPasswordNotMatchException.class)
    public ErrorDTO handlePasswordNotMatchException(AuthPasswordNotMatchException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Password do usuário não correspondem");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUserUsernameExistsException.class)
    public ErrorDTO handleAuthUserUsernameExistsException(AuthUserUsernameExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Username já existe");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthEmailExistsException.class)
    public ErrorDTO handleAuthUserEmailExistsException(AuthEmailExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe");
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
    @ExceptionHandler(AuthUserCustomerConflictException.class)
    public ErrorDTO handleAuthUserCustomerConflictException(AuthUserCustomerConflictException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Usuário já vinculado a um cliente");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthRuleNotFoundException.class)
    public ErrorDTO handleAuthRuleNotFoundException(AuthRuleNotFoundException ex) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), String.format("Rule %s não encontrada", ex.getRuleName()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUserInvalidKeyTokenException.class)
    public ErrorDTO handleAuthUserInvalidKeyTokenException(AuthUserInvalidKeyTokenException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Chave de autenticação inválida");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleAuthUserInvalidKeyTokenException(CustomerInvalidKeyException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Chave de autenticação inválida");
    }
    //CustomerInvalidKeyException

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthConfigNotFoundException.class)
    public ErrorDTO handleAuthConfigNotFound(AuthConfigNotFoundException authConfigNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Configuração não encontrada");
    }

}
