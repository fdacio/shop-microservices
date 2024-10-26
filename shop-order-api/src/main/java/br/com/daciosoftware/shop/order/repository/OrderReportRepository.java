package br.com.daciosoftware.shop.order.repository;

import br.com.daciosoftware.shop.models.dto.order.OrderSummaryReportDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderCustomerReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface OrderReportRepository {

	OrderSummaryReportDTO getOrderByDate(LocalDate dataInicio, LocalDate dataFim);
	List<OrderCustomerReportDTO> getOrderCustomerByDate(LocalDate dataInicio, LocalDate dataFim);
}
