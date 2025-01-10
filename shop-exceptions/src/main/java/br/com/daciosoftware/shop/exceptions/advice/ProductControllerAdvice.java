package br.com.daciosoftware.shop.exceptions.advice;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.product.CategoryNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.ProductIdentifieViolationException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.ProductNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.ProductReportPdfException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"br.com.daciosoftware.shop.product.controller"})
public class ProductControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorDTO handleProductNotFound(HttpServletRequest request, ProductNotFoundException productNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Produto não encontrado", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorDTO handleCategoryNotFount(HttpServletRequest request,CategoryNotFoundException categoryNotFoundException) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Categoria não encontrada", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductIdentifieViolationException.class)
    public ErrorDTO handleIntegrityViolation(HttpServletRequest request, ProductIdentifieViolationException ex) {
        return new ErrorDTO(HttpStatus.CONFLICT.value(), "Identificador do produto já existe", request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(ProductReportPdfException.class)
    public ErrorDTO handleReportPdf(HttpServletRequest request, ProductReportPdfException ex) {
        return new ErrorDTO(HttpStatus.NO_CONTENT.value(), "Erro ao gerar relatório", request);
    }

}
