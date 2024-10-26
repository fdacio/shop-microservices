package br.com.daciosoftware.shop.models.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryReportDTO {
	private Integer count;
	private Float total;
	private Float mean;
}
