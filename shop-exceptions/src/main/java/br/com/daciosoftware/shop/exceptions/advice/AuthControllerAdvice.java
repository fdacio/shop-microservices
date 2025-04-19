package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.*;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerCpfExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import jakarta.servlet.http.HttpServletRequest;
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
    public ErrorDTO handleAuthUserNotFound(HttpServletRequest request, AuthUserNotFoundException authUserNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Usuário não encontrado", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthInvalidLoginException.class)
    public ErrorDTO handleAuthLoginInvalid(HttpServletRequest request, AuthInvalidLoginException authLoginErrorException) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Usuário ou senha inválido", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthPasswordNotMatchException.class)
    public ErrorDTO handlePasswordNotMatchException(HttpServletRequest request, AuthPasswordNotMatchException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Password do usuário não correspondem", request);
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
    @ExceptionHandler(AuthUserCustomerConflictException.class)
    public ErrorDTO handleAuthUserCustomerConflictException(HttpServletRequest request, AuthUserCustomerConflictException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Usuário já vinculado a um cliente", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthRuleNotFoundException.class)
    public ErrorDTO handleAuthRuleNotFoundException(HttpServletRequest request, AuthRuleNotFoundException ex) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), String.format("Rule %s não encontrada", ex.getRuleName()), request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUserInvalidKeyTokenException.class)
    public ErrorDTO handleAuthUserInvalidKeyTokenException(HttpServletRequest request, AuthUserInvalidKeyTokenException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Token inválido", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerInvalidKeyException.class)
    public ErrorDTO handleAuthUserInvalidKeyTokenException(HttpServletRequest request, CustomerInvalidKeyException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Chave de autenticação inválida", request);
    }
    //CustomerInvalidKeyException

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthConfigNotFoundException.class)
    public ErrorDTO handleAuthConfigNotFound(HttpServletRequest request, AuthConfigNotFoundException authConfigNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Configuração não encontrada", request);
    }


}
