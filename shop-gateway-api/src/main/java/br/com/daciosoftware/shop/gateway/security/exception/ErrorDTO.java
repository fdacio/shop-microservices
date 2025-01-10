package br.com.daciosoftware.shop.gateway.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.time.LocalDateTime;


public class ErrorDTO {
	private final int status;
	private final String statusName;
	private final String message;
	private final LocalDateTime date;
	private final String url;

	public ErrorDTO(int status, String message, ServerHttpRequest request) {
		this.status = status;
		this.statusName = HttpStatus.valueOf(status).getReasonPhrase();
		this.message = message;
		this.date = LocalDateTime.now();
		this.url = request.getURI().getPath();
	}

	public ErrorDTO(int status, String message, ServerRequest request) {
		this.status = status;
		this.statusName = HttpStatus.valueOf(status).getReasonPhrase();
		this.message = message;
		this.date = LocalDateTime.now();
		this.url = request.path();
	}

	public int getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return String.format(
				"{" +
				"  \"status\":\"%s\",  " +
				"  \"statusName\":\"%s\",  " +
				"  \"message\":\"%s\", "+
				"  \"date\":\"%s\", "+
				"  \"url\":\"%s\" "+
				'}', status, statusName, message, date, url);
	}
}
