package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.dto.ValidErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceAuthUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceCategoryUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceCustomerUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceProductUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
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
        "br.com.daciosoftware.shop.auth.controller"})
public class ApplicationControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceAuthUnavailableException.class)
    public ErrorDTO handleServiceAuthUnavailableException(HttpServletRequest request, ServiceAuthUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço auth indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceCustomerUnavailableException.class)
    public ErrorDTO handleServiceCustomerUnavailableException(HttpServletRequest request, ServiceCustomerUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço customer indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceProductUnavailableException.class)
    public ErrorDTO handleServiceProductUnavailableException(HttpServletRequest request, ServiceProductUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço product indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceCategoryUnavailableException.class)
    public ErrorDTO handleServiceCategoryUnavailableException(HttpServletRequest request, ServiceCategoryUnavailableException exception) {
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço category indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO handleIllegalArgumentError(HttpServletRequest request, MethodArgumentNotValidException ex) {
        ValidErrorDTO error = new ValidErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro de validação de campos", request);
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
    public ErrorDTO handleHttpMessageNotReadableError(HttpServletRequest request, HttpMessageNotReadableException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro no corpo da requisição", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDTO handleIllegalArgumentError(HttpServletRequest request, IllegalArgumentException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro no corpo da requisição", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorDTO handleMethodArgumentTypeMismatchError(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Requisição inválida", request);
    }


}
