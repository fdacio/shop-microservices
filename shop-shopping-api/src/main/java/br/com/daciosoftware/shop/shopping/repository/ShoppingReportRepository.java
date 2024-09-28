package br.com.daciosoftware.shop.shopping.repository;

import br.com.daciosoftware.shop.modelos.dto.shopping.ShopSummaryReportDTO;
import br.com.daciosoftware.shop.modelos.dto.shopping.ShopUserReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ShoppingReportRepository {

	ShopSummaryReportDTO getShopByDate(LocalDate dataInicio, LocalDate dataFim);
	List<ShopUserReportDTO> getShopUserByDate(LocalDate dataInicio, LocalDate dataFim);
}
