package br.com.daciosoftware.shop.shopping.repository;

import br.com.daciosoftware.shop.models.dto.shopping.ShopSummaryReportDTO;
import br.com.daciosoftware.shop.models.dto.shopping.ShopUserReportDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.util.List;

public class ShoppingReportRepositoryImpl implements ShoppingReportRepository {

	@PersistenceContext
	private EntityManager entityManager;
	

	@Override
	public ShopSummaryReportDTO getShopByDate(LocalDate dataInicio, LocalDate dataFim) {

        String sbSql = "select \n" +
                "count(s.id) as count, \n" +
                "sum(s.total) as total, \n" +
                "avg(s.total) as mean \n" +
                "from shop s \n" +
                "where s.data >= :dataInicio \n" +
                "and s.data <= :dataFim \n";
		
		Query query = entityManager.createQuery(sbSql);
		query.setParameter("dataInicio", dataInicio.atTime(0, 0));
		query.setParameter("dataFim", dataFim.atTime(23, 59));
		
		Object[] result = (Object[]) query.getSingleResult();

		Integer count = (result[0] != null) ? ((Long) result[0]).intValue() : 0;
		Float total = (result[1] != null) ? ((Double) result[1]).floatValue() : 0;
		Float mean = (result[2] != null) ? ((Double) result[2]).floatValue() : 0;
		
		ShopSummaryReportDTO shopReportDTO = new ShopSummaryReportDTO();
		shopReportDTO.setCount(count);
		shopReportDTO.setTotal(total);
		shopReportDTO.setMean(mean);
		
		return shopReportDTO;
		
	}

	@Override
	public List<ShopUserReportDTO> getShopUserByDate(LocalDate dataInicio, LocalDate dataFim) {
		return List.of();
	}

}
