package br.com.daciosoftware.shop.user;

import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import br.com.daciosoftware.shop.modelos.entity.user.User;
import br.com.daciosoftware.shop.user.controller.UserController;
import br.com.daciosoftware.shop.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void testFindAll() throws Exception {

		User user = UserServiceTest.getUser(1L, "Dacio Braga", "80978380363");
		User user2 = UserServiceTest.getUser(2L, "Pedro Artur", "09465998311");
		UserDTO userDTO = UserDTO.convert(user);
		UserDTO userDTO2 = UserDTO.convert(user2);
		List<UserDTO> users = new ArrayList<>();
		users.add(userDTO);
		users.add(userDTO2);

		Mockito.when(userService.findAll()).thenReturn(users);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String resp = mvcResult.getResponse().getContentAsString();

		Assertions.assertEquals(
	"["
				+ "{"
					+ "\"id\":1,"
					+ "\"nome\":\"Dacio Braga\","
					+ "\"cpf\":\"80978380363\","
					+ "\"endereco\":\"Rua Alberto Torres, 200\","
					+ "\"email\":\"usuario@exemplo.com\","
					+ "\"telefone\":\"(85) 9 9971-8151\","
					+ "\"dataCadastro\":null,"
					+ "\"interesses\":null"
				+ "},"
				+ "{"
					+ "\"id\":2,"
					+ "\"nome\":\"Pedro Artur\","
					+ "\"cpf\":\"09465998311\","
					+ "\"endereco\":\"Rua Alberto Torres, 200\","
					+ "\"email\":\"usuario@exemplo.com\","
					+ "\"telefone\":\"(85) 9 9971-8151\","
					+ "\"dataCadastro\":null,"
					+ "\"interesses\":null"
				+ "}"
			+ "]", 	resp);

	}

	@Test
	public void testSave() throws Exception {

		User user = UserServiceTest.getUser(1L, "Dacio Braga", "80978380363");
		UserDTO userDTO = UserDTO.convert(user);

		ObjectMapper mapper = new ObjectMapper();
		String payload = mapper.writeValueAsString(userDTO);

		Mockito.when(userService.save(Mockito.any())).thenReturn(userDTO);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders
						.post("/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

		String resp = mvcResult.getResponse().getContentAsString();

		Assertions.assertEquals(payload, resp);
	}

}
