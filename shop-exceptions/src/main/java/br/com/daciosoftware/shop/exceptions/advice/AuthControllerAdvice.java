package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthLoginErrorException;
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
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage("Usuário não encontrado");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthLoginErrorException.class)
    public ErrorDTO handleAuthLoginInvalid(AuthLoginErrorException authLoginErrorException) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage("Usuário ou senha inválido");
        error.setDate(LocalDateTime.now());
        return error;
    }
}
