package br.com.daciosoftware.shop.customer.controller;

import br.com.daciosoftware.shop.customer.service.CustomerService;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import br.com.daciosoftware.shop.models.dto.customer.CreateCustomerAndAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping
	public List<CustomerDTO> findAll() {
		return customerService.findAll();
	}
	
	@GetMapping("/{id}")
	public CustomerDTO findById(@PathVariable Long id) {
		return customerService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerDTO save(@RequestBody @Valid CustomerDTO customerDTO) {
		return customerService.save(customerDTO);
	}

	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateCustomerAndAuthUserDTO createCustomerAndAuthUser(@RequestBody @Valid CreateCustomerAndAuthUserDTO createCustomerAndAuthUserDTO) {
		return customerService.createCustomerAndAuthUser(createCustomerAndAuthUserDTO);
	}

	@PostMapping("/{customerId}/user")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthUserDTO createAuthUserFromCustomer(@PathVariable Long customerId, @RequestBody PasswordDTO password) {
		return customerService.createAuthUserFromCustomer(customerId, password);
	}

	@GetMapping("/{cpf}/cpf")
	public CustomerDTO findByCpf(@PathVariable String cpf) {
		return customerService.findByCpf(cpf);
	}
	
	@GetMapping("/{email}/email")
	public CustomerDTO findByEmail(@PathVariable String email) {
		return customerService.findByEmail(email);
	}

	@GetMapping("/{keyAuth}/key-auth")
	public CustomerDTO findByKeyAuth(@PathVariable String keyAuth) {
		return customerService.findByKeyAuth(keyAuth);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		customerService.delete(id);
	}

	@DeleteMapping("/{customerId}/user")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCustomerAndAuthUser(@PathVariable Long customerId) {
		customerService.deleteCustomerAndAuthUser(customerId);
	}
	@GetMapping("/search")
	public List<CustomerDTO> findByNone(@RequestParam(name = "nome") String nome) {
		return customerService.findByNome(nome);
	}
	
	@PatchMapping("/{id}")
	public CustomerDTO update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
		return customerService.update(id, customerDTO);
	}
	
	@GetMapping("/pageable")
	public Page<CustomerDTO> findAllPageable(Pageable page) {
		return customerService.findAllPageable(page);
	}

	@PostMapping("/valid-key-auth")
	public CustomerDTO validKeyAuth(@RequestHeader(name = "customerKeyAuth") String customerKeyAuth) {
		return customerService.findByKeyAuth(customerKeyAuth);
	}

	@GetMapping("/has-key-auth")
	public List<CustomerDTO> findCustomersWithKeyAuth() {
		return customerService.findHasKeyAuth();
	}

	@PatchMapping("/update-key-all")
	public List<CustomerDTO> updateKeyAll() {
		return customerService.updateKeyAll();
	}

	@GetMapping("/by-category/{categoryId}")
	public List<CustomerDTO> getCustomersByCategory(@PathVariable Long categoryId) {
		return customerService.findByCategory(categoryId);
	}

	@GetMapping("/grouping-by-category")
	public Map<CategoryDTO, List<CustomerDTO>> getCustomersGroupingByCategory() {
		return customerService.getCustomersGroupingByCategory();
	}

	@GetMapping("/healthcheck")
	public String healthcheck () {
		return "ok";
	}

}
