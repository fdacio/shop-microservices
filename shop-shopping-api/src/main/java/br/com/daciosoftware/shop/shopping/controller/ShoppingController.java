package br.com.daciosoftware.shop.shopping.controller;

import br.com.daciosoftware.shop.models.dto.shopping.ShopDTO;
import br.com.daciosoftware.shop.shopping.service.ShoppingService;
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
@RequestMapping("/shopping")
public class ShoppingController {

	@Autowired
	private ShoppingService shoppingService; 
	
	@GetMapping
	public List<ShopDTO> findAll() {		
		return shoppingService.findAll();
	}
	
	@GetMapping("/{id}")
	public ShopDTO findById(@PathVariable Long id) {
		return shoppingService.findById(id);
	}
	
	@GetMapping("/pageable")
	public Page<ShopDTO> findAllPageable(Pageable pageable) {
		return shoppingService.findAllPageable(pageable);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ShopDTO save(@Valid @RequestBody ShopDTO shopDTO, @RequestHeader("Authorization") String token) {
		return shoppingService.save(shopDTO, token);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		shoppingService.delete(id);
	}
	
	@GetMapping("/customer/{customerId}")
	public List<ShopDTO> findByCustomerIndentifier(@PathVariable Long customerId) {
		return shoppingService.findByCustomerIndentifier(customerId);
	}

	@GetMapping("/filters")
	public List<ShopDTO> findShopsByFilters(
			@RequestParam(name = "dataInicio", required = true)
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataInicio,
			@RequestParam(name = "dataFim", required = false)
			@DateTimeFormat(pattern = "dd/MM/yyyy")
			LocalDate dataFim,
			@RequestParam(name = "valorMinimo", required = false)
			Float valorMinimo)
	{
		return shoppingService.findShopsByFilters(dataInicio, dataFim, valorMinimo);
	}

	@GetMapping("/healthcheck")
	public String healthcheck () {
		return "ok";
	}
}
