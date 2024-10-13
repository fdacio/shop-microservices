package br.com.daciosoftware.shop.customer.controller;

import br.com.daciosoftware.shop.customer.service.CustomerService;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import br.com.daciosoftware.shop.models.dto.customer.CreateCustomerUserDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
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
	public CreateCustomerUserDTO createCustomerAndAuthUser(@RequestBody @Valid CreateCustomerUserDTO createCustomerUserDTO) {
		return customerService.createCustomerAndAuthUser(createCustomerUserDTO);
	}

	@PostMapping("/{id}/user")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthUserDTO createAuthUser(@PathVariable Long id, @RequestBody PasswordDTO password) {
		return customerService.createAuthUser(id, password);
	}

	@GetMapping("/{cpf}/cpf")
	public CustomerDTO findByCpf(@PathVariable String cpf) {
		return customerService.findByCpf(cpf);
	}
	
	@GetMapping("/{email}/email")
	public CustomerDTO findByEmail(@PathVariable String email) {
		return customerService.findByEmail(email);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		customerService.delete(id);
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
	public Page<CustomerDTO> getcustomerPage(Pageable page) {
		return customerService.getAllPage(page);
	}

	@PostMapping("/valid-key-auth")
	public CustomerDTO validaKeyAuth(@RequestHeader(name = "customerKeyAuth") String customerKeyAuth) {
		return customerService.findByKeyAuth(customerKeyAuth);
	}

	@GetMapping("/{customerKeyAuth}/key-token")
	public CustomerDTO findByKeyToken(@PathVariable String customerKeyAuth) {
		return customerService.findByKeyAuth(customerKeyAuth);
	}

	@PatchMapping("/update-key-all")
	public List<CustomerDTO> updateKeyAll() {
		return customerService.updateKeyAll();
	}

	@GetMapping("/by-category")
	public Map<String, List<CustomerDTO>> getCustomersGroupByCategory() {
		return customerService.getCustomersGroupByCategory();
	}

	@GetMapping("/healthcheck")
	public String healthcheck () {
		return "ok";
	}

}
