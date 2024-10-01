package br.com.daciosoftware.shop.models.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductReportRequestDTO {

	private String descricao;
	private CategoryDTO category;
	private Float precoInicial;
	private Float precoFinal;
}
