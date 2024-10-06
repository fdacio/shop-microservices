package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.UserCpfExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.UserEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.UserNotFoundException;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.product.Category;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CategoryService categoryService;

    public List<CustomerDTO> findAll() {
		return customerRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(Customer::getId))
				.map(CustomerDTO::convert)
				.collect(Collectors.toList());	
	}

	public CustomerDTO findById(Long userId) {
		return customerRepository.findById(userId)
				.map(CustomerDTO::convert)
				.orElseThrow(UserNotFoundException::new);
	}

	public List<CustomerDTO> findByNome(String nome) {
		return customerRepository.findByNomeContainingIgnoreCase(nome)
				.stream()
				.map(CustomerDTO::convert)
				.collect(Collectors.toList());
	}

	public CustomerDTO findByCpf(String cpf) {
		return customerRepository.findByCpf(cpf)
				.map(CustomerDTO::convert)
				.orElseThrow(UserNotFoundException::new);
	}
	
	public CustomerDTO findByEmail(String email) {
		return customerRepository.findByEmail(email)
				.map(CustomerDTO::convert)
				.orElseThrow(UserNotFoundException::new);
	}
	
	private void validCpfUnique(String cpf) {
		Optional<CustomerDTO> userDTO = customerRepository.findByCpf(cpf).map(CustomerDTO::convert);
		if (userDTO.isPresent()) {
			throw new UserCpfExistsException();
		}
	}
	
	private void validEmailUnique(String email, Long id) {
		Optional<CustomerDTO> userDTO = customerRepository.findByEmail(email).map(CustomerDTO::convert);
		if (userDTO.isPresent()) {
			if (id == null) {
				throw new UserEmailExistsException();
			} else if (!id.equals(userDTO.get().getId())) {
				throw new UserEmailExistsException();
			}
		}
	}

	public CustomerDTO save(CustomerDTO customerDTO) {
		validCpfUnique(customerDTO.getCpf());
		validEmailUnique(customerDTO.getEmail(), null);
		customerDTO.setDataCadastro(LocalDateTime.now());
		customerDTO.setInteresses(categoryService.findCategorysByUser(customerDTO));
		return CustomerDTO.convert(customerRepository.save(Customer.convert(customerDTO)));
	}

	public void delete(Long userId) {
		customerRepository.delete(Customer.convert(findById(userId)));
	}

	public CustomerDTO update(Long userId, CustomerDTO userDTO) {
		
		Customer user = Customer.convert(findById(userId));
		
		if (userDTO.getEndereco() != null) { 
			boolean isEnderecoAlterado = !(user.getEndereco().equals(userDTO.getEndereco()));
			if (isEnderecoAlterado) {
				user.setEndereco(userDTO.getEndereco());
			}
		}
		
		if (userDTO.getEmail() != null) { 
			boolean isEmailAlterado = !(user.getEmail().equals(userDTO.getEmail()));
			if (isEmailAlterado) {
				validEmailUnique(userDTO.getEmail(), userId);
				user.setEmail(userDTO.getEmail());
			}
		}
		
		if (userDTO.getTelefone() != null) {
			boolean isTelefoneAlterado = !(user.getTelefone().equals(userDTO.getTelefone()));
			if (isTelefoneAlterado) {
				user.setTelefone(userDTO.getTelefone());
			}
		}
		
		if ((userDTO.getInteresses() != null)) {

			userDTO.setInteresses(categoryService.findCategorysByUser(userDTO));

			Set<Category> interesses = userDTO.getInteresses()
					.stream()
					.map(Category::convert)
					.collect(Collectors.toSet());

			user.setInteresses(interesses);
		}
		
		return CustomerDTO.convert(customerRepository.save(user));
	}

	public Page<CustomerDTO> getAllPage(Pageable page) {
		return customerRepository.findAll(page).map(CustomerDTO::convert);
	}

	public List<CustomerDTO> updateKeyAll() {
		List<Customer> users = customerRepository.findAll();
		return users.stream().map(u -> {
			u = customerRepository.save(u);
			return CustomerDTO.convert(u);
		}).collect(Collectors.toList());
	}

	public Map<String, List<CustomerDTO>> getCustomersGroupByCategory() {
		
		Map<String, List<CustomerDTO>> groupByCategory = new LinkedHashMap<>();
		
		List<CustomerDTO> users = findAll();
		
		List<CategoryDTO> categories = categoryService.findAll();
		
		categories.stream().sorted(Comparator.comparing(CategoryDTO::getId)).forEach(c -> {

			List<CustomerDTO> listUsers = new ArrayList<>();
			
			users.forEach(u -> {
				if (u.getInteresses().contains(c)) {
					listUsers.add(u);
				}
			});
			
			if (!listUsers.isEmpty()) {
				String category = String.format("%d -> %s", c.getId(), c.getNome());
				groupByCategory.put(category, listUsers);
			}
		});
		
		return groupByCategory;
		
	}

}
