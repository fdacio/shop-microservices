package br.com.daciosoftware.shop.user;

import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import br.com.daciosoftware.shop.modelos.entity.user.User;
import br.com.daciosoftware.shop.user.repository.UserRepository;
import br.com.daciosoftware.shop.user.service.CategoryService;
import br.com.daciosoftware.shop.user.service.UserService;
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
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private CategoryService categoryService;
	
	public static User getUser(Long id, String nome, String cpf) {
		User user = new User();
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
		
		List<User> users = new ArrayList<>();
		
		users.add(getUser(1L, "Jose Ribamar Silva", "87459974496"));
		users.add(getUser(2L, "Zenilde Martins Braga Silva", "74598786152"));
		users.add(getUser(3L, "Francisco Dacio M B Silva", "80978380363"));
		
		Mockito.when(userRepository.findAll()).thenReturn(users);
		
		List<UserDTO> usersReturn = userService.findAll();
		
		Assertions.assertEquals(3, usersReturn.size());
	}
	
	@Test
	public void testFindByIdUser() {		
		
		User user = getUser(3L, "Francisco Dacio M B Silva", "80978380363");
		
		Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
		
		UserDTO userReturn = userService.findById(3L);
		
		Assertions.assertEquals(3L, userReturn.getId());
	}
	
	@Test
	public void testFindUserCpf() {
		
		User user = getUser(3L, "Francisco Dacio M B Silva", "80978380363");
		
		
		Mockito.when(userRepository.findByCpf("80978380363")).thenReturn(Optional.of(user));
		
		UserDTO userReturn = userService.findByCpf("80978380363");
		
		Assertions.assertEquals("80978380363", userReturn.getCpf());
	}
	
	@Test
	public void testSaveUser() {
		
		User user = getUser(null, "Francisco Dacio M B Silva", "80978380363");	
		
		UserDTO userDTO = UserDTO.convert(user);
		
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		
		UserDTO userReturn = userService.save(userDTO);
		
		Assertions.assertEquals("Francisco Dacio M B Silva", userReturn.getNome());
		Assertions.assertEquals("80978380363", userReturn.getCpf());
		
	}
	
	@Test void testUpdateUser() {
		
		User user = getUser(3L, "Francisco Dacio M B Silva", "80978380363");
		
		Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		
		user.setEmail("fdacio@gmail.com");
		user.setEndereco("Rua Guaporé, 956");
		
		UserDTO userDTO = UserDTO.convert(user);
		UserDTO userReturn = userService.update(3L, userDTO);
		
		Assertions.assertEquals("Rua Guaporé, 956", userReturn.getEndereco());
		Assertions.assertEquals("fdacio@gmail.com", userReturn.getEmail());
		
	}
}
