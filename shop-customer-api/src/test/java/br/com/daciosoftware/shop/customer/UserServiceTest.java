package br.com.daciosoftware.shop.customer;

import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.customer.repository.CustomerRepository;
import br.com.daciosoftware.shop.customer.service.CategoryService;
import br.com.daciosoftware.shop.customer.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private CustomerService userService;
	
	@Mock
	private CustomerRepository userRepository;

	@Mock
	private CategoryService categoryService;
	
	public static Customer getUser(Long id, String nome, String cpf) {
		Customer user = new Customer();
		user.setId(id);
		user.setNome(nome);
		user.setCpf(cpf);
		user.setEmail("usuario@exemplo.com");
		user.setEndereco("Rua Alberto Torres, 200");
		user.setTelefone("(85) 9 9971-8151");
		return user;
	}
	
	@Test
	public void testFindAllUser() {
		
		List<Customer> users = new ArrayList<>();
		
		users.add(getUser(1L, "Jose Ribamar Silva", "87459974496"));
		users.add(getUser(2L, "Zenilde Martins Braga Silva", "74598786152"));
		users.add(getUser(3L, "Francisco Dacio M B Silva", "80978380363"));
		
		Mockito.when(userRepository.findAll()).thenReturn(users);
		
		List<CustomerDTO> usersReturn = userService.findAll();
		
		Assertions.assertEquals(3, usersReturn.size());
	}
	
	@Test
	public void testFindByIdUser() {		
		
		Customer user = getUser(3L, "Francisco Dacio M B Silva", "80978380363");
		
		Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
		
		CustomerDTO userReturn = userService.findById(3L);
		
		Assertions.assertEquals(3L, userReturn.getId());
	}
	
	@Test
	public void testFindUserCpf() {
		
		Customer user = getUser(3L, "Francisco Dacio M B Silva", "80978380363");
		
		
		Mockito.when(userRepository.findByCpf("80978380363")).thenReturn(Optional.of(user));
		
		CustomerDTO userReturn = userService.findByCpf("80978380363");
		
		Assertions.assertEquals("80978380363", userReturn.getCpf());
	}
	
	@Test
	public void testSaveUser() {
		
		Customer user = getUser(null, "Francisco Dacio M B Silva", "80978380363");
		
		CustomerDTO userDTO = CustomerDTO.convert(user);
		
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		
		CustomerDTO userReturn = userService.save(userDTO);
		
		Assertions.assertEquals("Francisco Dacio M B Silva", userReturn.getNome());
		Assertions.assertEquals("80978380363", userReturn.getCpf());
		
	}
	
	@Test void testUpdateUser() {
		
		Customer user = getUser(3L, "Francisco Dacio M B Silva", "80978380363");
		
		Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		
		user.setEmail("fdacio@gmail.com");
		user.setEndereco("Rua Guaporé, 956");
		
		CustomerDTO userDTO = CustomerDTO.convert(user);
		CustomerDTO userReturn = userService.update(3L, userDTO);
		
		Assertions.assertEquals("Rua Guaporé, 956", userReturn.getEndereco());
		Assertions.assertEquals("fdacio@gmail.com", userReturn.getEmail());
		
	}
}
