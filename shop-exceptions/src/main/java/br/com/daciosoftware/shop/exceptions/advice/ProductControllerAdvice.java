package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.CategoryNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ProductIdentifieViolationException;
import br.com.daciosoftware.shop.exceptions.exceptions.ProductNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ReportPdfException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = { "br.com.daciosoftware.shop.product.controller" })
public class ProductControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorDTO handleProductNotFound(ProductNotFoundException productNotFoundException) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage("Produto não encontrado");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorDTO handleCategoryNotFount(CategoryNotFoundException categoryNotFoundException) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage("Categoria não encontrada");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductIdentifieViolationException.class)
    public ErrorDTO handleIntegrityViolation(ProductIdentifieViolationException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage("Identificador do produto já existe");
        error.setDate(LocalDateTime.now());
        return error;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(ReportPdfException.class)
    public ErrorDTO handleReportPdf(ReportPdfException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NO_CONTENT.value());
        error.setMessage("Erro ao gerar relatório");
        error.setDate(LocalDateTime.now());
        return error;
    }

}
