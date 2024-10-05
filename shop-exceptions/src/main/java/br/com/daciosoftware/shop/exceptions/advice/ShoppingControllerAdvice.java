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
		return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "SHOP - Venda não encontrado");

	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public ErrorDTO handleUserNotFound(UserNotFoundException userNotFoundException) {
		return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "SHOP - Usuário não encontrado");
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ProductNotFoundException.class)
	public ErrorDTO handleProductNotFound(ProductNotFoundException productNotFoundException) {
		return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "SHOP - Produto não encontrado");
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(InvalidUserKeyException.class)
	public ErrorDTO handleInvalidKeyCustomer(InvalidUserKeyException invalidUserKeyException) {
		return new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "SHOP - Chave do usuário inválida");

	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ErrorDTO handleInvalidRequest(MethodArgumentTypeMismatchException ex) {
		return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "SHOP - Requisição inválida");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ErrorDTO handleInvalidMessageRequest(HttpMessageNotReadableException ex) {
		return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "SHOP - Requisição inválida");
	}
	
}
