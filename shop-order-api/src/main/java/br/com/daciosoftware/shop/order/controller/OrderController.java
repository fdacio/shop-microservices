package br.com.daciosoftware.shop.order.controller;

import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping
	public List<OrderDTO> findAll() {
		return orderService.findAll();
	}

	@GetMapping("/my-orders")
	@ResponseStatus(HttpStatus.OK)
	public List<OrderDTO> findOrdersCustomerAuthenticated(@RequestHeader("Authorization") String token) {
		return orderService.findOrdersCustomerAuthenticated(token);
	}

	@GetMapping("/{id}")
	public OrderDTO findById(@PathVariable Long id) {
		return orderService.findById(id);
	}

	@GetMapping("/{id}/my-order")
	public OrderDTO findById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		return orderService.findById(id, token);
	}

	@GetMapping("/pageable")
	public Page<OrderDTO> findAllPageable(Pageable pageable) {
		return orderService.findAllPageable(pageable);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDTO save(@Valid @RequestBody OrderDTO orderDTO, @RequestHeader("Authorization") String token) {
		return orderService.save(orderDTO, token);
	}

	@PatchMapping("/{id}/my-order")
	@ResponseStatus(HttpStatus.OK)
	public OrderDTO update(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO, @RequestHeader("Authorization") String token) {
		return orderService.update(id, orderDTO, token);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public OrderDTO update(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
		return orderService.update(id, orderDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		orderService.delete(id);
	}

	@DeleteMapping("/{id}/my-order")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		orderService.delete(id, token);
	}

	@GetMapping("/customer/{customerId}")
	public List<OrderDTO> findByCustomerIndentifier(@PathVariable Long customerId) {
		return orderService.findByCustomerIndentifier(customerId);
	}

	@GetMapping("/filters")
	public List<OrderDTO> findOrdersByFilters(
			@RequestParam(name = "dataInicio")
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataInicio,
			@RequestParam(name = "dataFim", required = false)
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataFim,
			@RequestParam(name = "valorMinimo", required = false)
			Float valorMinimo)
	{
		return orderService.findOrdersByFilters(dataInicio, dataFim, valorMinimo);
	}

	@GetMapping("/healthcheck")
	public String healthcheck () {
		return "ok";
	}
}
