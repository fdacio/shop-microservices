package br.com.daciosoftware.shop.product.config;

import br.com.daciosoftware.shop.models.entity.product.Category;
import br.com.daciosoftware.shop.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class CategoriesConfig implements CommandLineRunner {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        String[] categories = {"Eletrônicos", "Móveis", "Brinquedos", "Vestuários"};
        long id = 1L;
        for (String value : categories) {
            Optional<Category> optional = categoryRepository.findByNome(value);
            if (optional.isEmpty()) {
                Category category = new Category();
                category.setId(id);
                category.setNome(value);
                categoryRepository.save(category);
            }
        }

    }
}
