package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.dto.ValidErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserIntegrityViolationException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerIntegrityViolationException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceAuthUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceCategoryUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceCustomerUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceProductUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ApplicationControllerAdvice.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceAuthUnavailableException.class)
    public ErrorDTO handleServiceAuthUnavailableException(HttpServletRequest request, MicroserviceAuthUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço auth indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceCustomerUnavailableException.class)
    public ErrorDTO handleServiceCustomerUnavailableException(HttpServletRequest request, MicroserviceCustomerUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço customer indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceProductUnavailableException.class)
    public ErrorDTO handleServiceProductUnavailableException(HttpServletRequest request, MicroserviceProductUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço product indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceCategoryUnavailableException.class)
    public ErrorDTO handleServiceCategoryUnavailableException(HttpServletRequest request, MicroserviceCategoryUnavailableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), "Serviço category indisponível", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO handleIllegalArgumentError(HttpServletRequest request, MethodArgumentNotValidException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        ValidErrorDTO error = new ValidErrorDTO(HttpStatus.BAD_REQUEST.value(), "Erro de validação de campos", request);
        Map<String, String> fieldsValidation = new HashMap<>();
        BindingResult result = exception.getBindingResult();
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
    public ErrorDTO handleHttpMessageNotReadableError(HttpServletRequest request, HttpMessageNotReadableException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
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
    public ErrorDTO handleMethodArgumentTypeMismatchError(HttpServletRequest request, MethodArgumentTypeMismatchException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Requisição inválida", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorDTO handleDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Violação de integridade. O recurso está relacionado a outro", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerIntegrityViolationException.class)
    public ErrorDTO handleCustomerIntegrityViolationException(HttpServletRequest request, CustomerIntegrityViolationException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Violação de integridade. Cliente está relacionado a outro recurso", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AuthUserIntegrityViolationException.class)
    public ErrorDTO handleAuthUserIntegrityViolationException(HttpServletRequest request, AuthUserIntegrityViolationException exception) {
        log.error("Shop Error: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Violação de integridade. Usuário está relacionado a outro recurso", request);
    }

}
