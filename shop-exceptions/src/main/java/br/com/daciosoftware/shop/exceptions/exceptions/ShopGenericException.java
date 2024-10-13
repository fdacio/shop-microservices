package br.com.daciosoftware.shop.exceptions.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ShopGenericException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2518224431370285888L;

    private final String message;

    public ShopGenericException( String message) {
        this.message = message;
    }


}
