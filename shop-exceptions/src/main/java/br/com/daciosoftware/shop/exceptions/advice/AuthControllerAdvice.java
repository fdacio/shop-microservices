package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthExpiredTokenException;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthInvalidLoginException;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
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
    public ErrorDTO handleAuthExpiereTokek(AuthExpiredTokenException authExpiredTokenException) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Token expirado. Refaça o login");
    }
}
