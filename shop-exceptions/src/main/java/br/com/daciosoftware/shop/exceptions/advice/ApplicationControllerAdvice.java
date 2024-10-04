package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.dto.ValidErrorDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackages = {
		"br.com.daciosoftware.shop.user.controller",
		"br.com.daciosoftware.shop.product.controller",
		"br.com.daciosoftware.shop.shopping.controller",
		"br.com.daciosoftware.shop.auth.controller",
		"br.com.daciosoftware.shop.models.*" })
public class ApplicationControllerAdvice {
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorDTO handleValidationError(MethodArgumentNotValidException ex) {
		ValidErrorDTO error = new ValidErrorDTO();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("Erro de validação de campos");
		error.setDate(LocalDateTime.now());
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
	public ErrorDTO handleValidationError(HttpMessageNotReadableException ex) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("Erro no corpo da requisição");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorDTO handleValidationError(IllegalArgumentException ex) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("Erro no corpo da requisição");
		error.setDate(LocalDateTime.now());
		return error;
	}

	
	
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorDTO handleIntegrityViolation(DataIntegrityViolationException ex) {
		ErrorDTO error = new ErrorDTO();
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setMessage("Violação de integridade");
		error.setDate(LocalDateTime.now());
		return error;
	}
	
}
