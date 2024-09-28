package br.com.daciosoftware.shop.product.controller;

import br.com.daciosoftware.shop.modelos.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.product.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public List<CategoryDTO> getAll() {		
		return categoryService.findAll();		
	}
	
	@GetMapping("/{id}")
	public CategoryDTO findById(@PathVariable Long id) {
		return categoryService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDTO save(@Valid @RequestBody CategoryDTO categoryDTO) {
		return categoryService.save(categoryDTO);
	}

	@PatchMapping("/{id}")
	public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
		return categoryService.update(id, categoryDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		categoryService.delete(id);
	}
}
