package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.dto.ValidErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackages = {
		"br.com.daciosoftware.shop.customer.controller",
		"br.com.daciosoftware.shop.product.controller",
		"br.com.daciosoftware.shop.order.controller",
		"br.com.daciosoftware.shop.auth.controller",
		"br.com.daciosoftware.shop.models.*" })
public class ApplicationControllerAdvice {


	@ResponseBody
	@ExceptionHandler(ShopGenericException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ErrorDTO handleShopGenericError(ShopGenericException ex) {
		return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage());
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorDTO handleIllegalArgumentError(MethodArgumentNotValidException ex) {
		ValidErrorDTO error = new ValidErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro de validação de campos");
		Map<String, String> fieldsValidation = new HashMap<>();
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldsErrors = result.getFieldErrors();
		for (FieldError fieldError : fieldsErrors) {
			String name = fieldError.getField();
			String message = fieldError.getDefaultMessage();
			fieldsValidation.put(name, message);
		}
		error.setFields(fieldsValidation);
		return error;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ErrorDTO handleHttpMessageNotReadableError(HttpMessageNotReadableException ex) {
		return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro no corpo da requisição");
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorDTO handleIllegalArgumentError(IllegalArgumentException ex) {
		return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro no corpo da requisição");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ErrorDTO handleMethodArgumentTypeMismatchError(MethodArgumentTypeMismatchException ex) {
		return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Requisição inválida");
	}




}
