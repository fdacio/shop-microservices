package br.com.daciosoftware.shop.models.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING(0, "Pendente"),
    APPROVED(1, "Aprovado"),
    REJECTED(2, "Rejeitado"),
    SHIPPED(3, "Enviado"),
    DELIVERED(4, "Entregue"),;

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
