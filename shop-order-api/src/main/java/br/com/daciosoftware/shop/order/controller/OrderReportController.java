package br.com.daciosoftware.shop.order.controller;

import br.com.daciosoftware.shop.order.service.OrderReportService;
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
@RequestMapping("/order/report")
public class OrderReportController {

	@Autowired
	OrderReportService orderReportService;
	
	@GetMapping("/demo/{id}")
	public ResponseEntity<?> getReportDemoVenda(@PathVariable Long id) {
		try {

			ByteArrayOutputStream pdfStream = orderReportService.getReportDemoVenda(id);
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
			@RequestParam(name = "dataInicio")
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataInicio, 
			@RequestParam(name = "dataFim")
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataFim) 
	{
		
		try {
			
	        ByteArrayOutputStream pdfStream = orderReportService.getReportResumoVendas(dataInicio, dataFim);
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
