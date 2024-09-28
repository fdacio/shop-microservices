package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.user.controller"})
public class UserControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorDTO handleUserNotFound(UserNotFoundException userNotFoundException) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage("Usuário não encontrado");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorDTO handleCategoryNotFount(CategoryNotFoundException categoryNotFoundException) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage("Categoria dos interesses não encontrada");
        error.setDate(LocalDateTime.now());
        return error;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidUserKeyException.class)
    public ErrorDTO handleProductNotFound(InvalidUserKeyException invalidUserKeyException) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage("Token Inválido");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserCpfExistsException.class)
    public ErrorDTO handleUserCpfExistesException(UserCpfExistsException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("CPF já existe");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailExistsException.class)
    public ErrorDTO handleUserEmailExistesException(UserEmailExistsException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Email já existe");
        error.setDate(LocalDateTime.now());
        return error;
    }

}
