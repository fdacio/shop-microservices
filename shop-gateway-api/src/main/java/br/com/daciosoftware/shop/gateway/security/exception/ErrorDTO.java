package br.com.daciosoftware.shop.gateway.security.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public class ErrorDTO {
	private final int status;
	private final String statusName;
	private final String message;
	private final LocalDateTime date;

	public ErrorDTO(int status, String message) {
		this.status = status;
		this.statusName = HttpStatus.valueOf(status).getReasonPhrase();
		this.message = message;
		this.date = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return String.format(
				"{" +
				"  \"status\":\"%s\",  " +
				"  \"statusName\":\"%s\",  " +
				"  \"message\":\"%s\", "+
				"  \"date\":\"%s\" "+
				'}', status, statusName, message, date);
	}
}
