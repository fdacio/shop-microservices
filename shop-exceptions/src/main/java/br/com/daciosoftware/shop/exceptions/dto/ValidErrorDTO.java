package br.com.daciosoftware.shop.exceptions.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidErrorDTO extends ErrorDTO {

	private Map<String, String> fields;

	public ValidErrorDTO(int status, String message, HttpServletRequest request) {
		super(status, message, request);
	}
}
