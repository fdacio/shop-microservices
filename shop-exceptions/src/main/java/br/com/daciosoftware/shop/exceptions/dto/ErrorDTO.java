package br.com.daciosoftware.shop.exceptions.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
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

}
