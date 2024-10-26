package br.com.daciosoftware.shop.order.repository;

import br.com.daciosoftware.shop.models.dto.order.OrderSummaryReportDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderCustomerReportDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.util.List;

public class OrderReportRepositoryImpl implements OrderReportRepository {

	@PersistenceContext
	private EntityManager entityManager;
	

	@Override
	public OrderSummaryReportDTO getOrderByDate(LocalDate dataInicio, LocalDate dataFim) {

        String sbSql = "select \n" +
                "count(o.id) as count, \n" +
                "sum(o.total) as total, \n" +
                "avg(o.total) as mean \n" +
                "from orders o \n" +
                "where o.data >= :dataInicio \n" +
                "and o.data <= :dataFim \n";
		
		Query query = entityManager.createQuery(sbSql);
		query.setParameter("dataInicio", dataInicio.atTime(0, 0));
		query.setParameter("dataFim", dataFim.atTime(23, 59));
		
		Object[] result = (Object[]) query.getSingleResult();

		Integer count = (result[0] != null) ? ((Long) result[0]).intValue() : 0;
		Float total = (result[1] != null) ? ((Double) result[1]).floatValue() : 0;
		Float mean = (result[2] != null) ? ((Double) result[2]).floatValue() : 0;
		
		OrderSummaryReportDTO shopReportDTO = new OrderSummaryReportDTO();
		shopReportDTO.setCount(count);
		shopReportDTO.setTotal(total);
		shopReportDTO.setMean(mean);
		
		return shopReportDTO;
		
	}

	@Override
	public List<OrderCustomerReportDTO> getOrderCustomerByDate(LocalDate dataInicio, LocalDate dataFim) {
		return List.of();
	}

}
