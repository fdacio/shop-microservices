package br.com.daciosoftware.shop.shopping.repository;

import br.com.daciosoftware.shop.models.dto.shopping.ShopSummaryReportDTO;
import br.com.daciosoftware.shop.models.dto.shopping.ShopCustomerReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ShoppingReportRepository {

	ShopSummaryReportDTO getShopByDate(LocalDate dataInicio, LocalDate dataFim);
	List<ShopCustomerReportDTO> getShopCustomerByDate(LocalDate dataInicio, LocalDate dataFim);
}
