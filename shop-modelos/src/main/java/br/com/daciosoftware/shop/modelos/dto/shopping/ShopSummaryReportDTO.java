package br.com.daciosoftware.shop.modelos.dto.shopping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopSummaryReportDTO {
	private Integer count;
	private Float total;
	private Float mean;
}
