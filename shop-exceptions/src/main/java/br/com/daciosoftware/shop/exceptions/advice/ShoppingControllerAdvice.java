package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.InvalidUserKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.ProductNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.shopping.controller"})
public class ShoppingControllerAdvice {
	
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ShopNotFoundException.class)
	public ErrorDTO handleUserNotFound(ShopNotFoundException shopNotFoundException) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage("SHOP - Venda não encontrado");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public ErrorDTO handleUserNotFound(UserNotFoundException userNotFoundException) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage("SHOP - Usuário não encontrado");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ProductNotFoundException.class)
	public ErrorDTO handleProductNotFound(ProductNotFoundException productNotFoundException) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage("SHOP - Produto não encontrado");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(InvalidUserKeyException.class)
	public ErrorDTO handleProductNotFound(InvalidUserKeyException invalidUserKeyException) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage("SHOP - Chave do usuário inválida");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ErrorDTO invalidaRequest(MethodArgumentTypeMismatchException ex) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("SHOP - Requisição inválida");
		error.setDate(LocalDateTime.now());
		return error;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ErrorDTO invalidaRequest(HttpMessageNotReadableException ex) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("SHOP - Requisição inválida");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
}
