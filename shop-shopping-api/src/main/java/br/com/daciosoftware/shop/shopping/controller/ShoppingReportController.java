package br.com.daciosoftware.shop.shopping.controller;

import br.com.daciosoftware.shop.shopping.service.ShoppingReportService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("/shopping/report")
public class ShoppingReportController {

	@Autowired
	ShoppingReportService shoppingReportService;
	
	@GetMapping("/demo/{id}")
	public ResponseEntity<?> getReportDemoVenda(@PathVariable Long id) {
		try {

			ByteArrayOutputStream pdfStream = shoppingReportService.getReportDemoVenda(id);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shop-demo.pdf");
			headers.setContentLength(pdfStream.size());

			return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

		} catch (DocumentException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}

	}

	@GetMapping("/periodo")
	public ResponseEntity<?> getReportResumoVendas(
			@RequestParam(name = "dataInicio", required = true) 
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataInicio, 
			@RequestParam(name = "dataFim", required = true) 
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataFim) 
	{
		
		try {
			
	        ByteArrayOutputStream pdfStream = shoppingReportService.getReportResumoVendas(dataInicio, dataFim);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shop-resume.pdf");
	        headers.setContentLength(pdfStream.size());
	        
	        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
	        
		} catch (DocumentException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}
		
	}
}
