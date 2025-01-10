package br.com.daciosoftware.shop.exceptions.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter

public class ErrorDTO {

	private final int status;
	private final String statusName;
	private final String message;
	private final LocalDateTime date;
	private final String url;

	public ErrorDTO(int status, String message, HttpServletRequest request) {
		this.status = status;
		this.statusName = HttpStatus.valueOf(status).getReasonPhrase();
		this.message = message;
		this.date = LocalDateTime.now();
		this.url = request.getServletPath();
	}

}
