package br.com.daciosoftware.shop.user.controller;

import br.com.daciosoftware.shop.modelos.dto.user.UserDTO;
import br.com.daciosoftware.shop.modelos.dto.user.UserWithKeyDTO;
import br.com.daciosoftware.shop.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<UserDTO> findAll() {
		return userService.findAll();
	}
	
	@GetMapping("/{id}")
	public UserDTO findById(@PathVariable Long id) {
		return userService.findById(id);
	}
	@GetMapping("/{id}/with-key")
	public UserWithKeyDTO findWithKeyById(@PathVariable Long id) {
		return userService.findByIdWithKey(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO save(@RequestBody @Valid UserDTO userDTO) {
		return userService.save(userDTO);
	}
	
	@GetMapping("/{cpf}/cpf")
	public UserDTO findByCpf(@PathVariable String cpf) {
		return userService.findByCpf(cpf);
	}
	
	@GetMapping("/{email}/email")
	public UserDTO findByEmail(@PathVariable String email) {
		return userService.findByEmail(email);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userService.delete(id);
	}
	
	@GetMapping("/search")
	public List<UserDTO> findByNone(@RequestParam(name = "nome") String nome) {
		return userService.findByNome(nome);
	}
	
	@PatchMapping("/{id}")
	public UserDTO update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
		return userService.update(id, userDTO);
	}
	
	@GetMapping("/pageable")
	public Page<UserDTO> getUserPage(Pageable page) {
		return userService.getAllPage(page);
	}	
	
	@PatchMapping("/update-key-all")
	public List<UserDTO> updateKeyAll() {
		return userService.updateKeyAll();
	}
	
	@PostMapping("/valid")
	public UserDTO validUser(@RequestBody UserDTO userDTO, @RequestHeader() String key) {
		return userService.validUserKey(userDTO, key);
	}
	
	@GetMapping("/by-category")
	public Map<String, List<UserDTO>> getUsersGroupByCategory() {
		return userService.getUsersGroupByCategory();
	}
}
