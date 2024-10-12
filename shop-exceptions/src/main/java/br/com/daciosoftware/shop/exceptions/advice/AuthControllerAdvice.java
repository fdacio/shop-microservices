package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

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
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthExpiredTokenException.class)
    public ErrorDTO handleAuthExpireToken(AuthExpiredTokenException authExpiredTokenException) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Token expirado. Refaça o login");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthPasswordNotMatchException.class)
    public ErrorDTO handlePasswordNotMatchException(AuthPasswordNotMatchException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Password do usuário não correspondem");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUsernameExistsException.class)
    public ErrorDTO handleAuthUserUsernameExistsException(AuthUsernameExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Username já existe");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthEmailExistsException.class)
    public ErrorDTO handleAuthUserEmailExistsException(AuthEmailExistsException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Email já existe");
    }
}
