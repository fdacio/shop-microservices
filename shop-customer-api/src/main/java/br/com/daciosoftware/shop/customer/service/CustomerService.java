package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.customer.repository.CustomerRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthPasswordNotMatchException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.*;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import br.com.daciosoftware.shop.models.dto.customer.CreateCustomerUserDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.models.entity.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuthService authService;

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Customer::getNome))
                .map(CustomerDTO::convert)
                .collect(Collectors.toList());
    }

    public CustomerDTO findById(Long userId) {
        return customerRepository.findById(userId)
                .map(CustomerDTO::convert)
                .orElseThrow(CustomerNotFoundException::new);
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
                .orElseThrow(CustomerNotFoundException::new);
    }

    public CustomerDTO findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(CustomerDTO::convert)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public CustomerDTO findByKeyAuth(String keyAuth) {
        return customerRepository.findByKeyAuth(keyAuth)
                .map(CustomerDTO::convert)
                .orElseThrow(CustomerInvalidKeyException::new);
    }

    public List<CustomerDTO> findHasKeyAuth() {
        return customerRepository.findHasKeyAuth()
                .stream()
                .map(CustomerDTO::convert)
                .sorted(Comparator.comparing(CustomerDTO::getNome))
                .toList();
    }

    private void validCpfUnique(String cpf) {
        Optional<CustomerDTO> userDTO = customerRepository.findByCpf(cpf).map(CustomerDTO::convert);
        if (userDTO.isPresent()) {
            throw new CustomerCpfExistsException();
        }
    }

    private void validEmailUnique(String email, Long id) {
        Optional<CustomerDTO> userDTO = customerRepository.findByEmail(email).map(CustomerDTO::convert);
        if (userDTO.isPresent()) {
            if (id == null) {
                throw new CustomerEmailExistsException();
            } else if (!id.equals(userDTO.get().getId())) {
                throw new CustomerEmailExistsException();
            }
        }
    }

    @Transactional
    public CustomerDTO save(CustomerDTO customerDTO) {
        validCpfUnique(customerDTO.getCpf());
        validEmailUnique(customerDTO.getEmail(), null);
        customerDTO.setDataCadastro(LocalDateTime.now());
        if (!customerDTO.getInteresses().isEmpty()) {
            Set<CategoryDTO> interesses = categoryService.validCategorys(customerDTO.getInteresses());
            customerDTO.setInteresses(interesses);
        }
        return CustomerDTO.convert(customerRepository.save(Customer.convert(customerDTO)));
    }

    public void delete(Long userId) {
        customerRepository.delete(Customer.convert(findById(userId)));
    }

    public CustomerDTO update(Long customerId, CustomerDTO customerDTO) {

        Customer customer = Customer.convert(findById(customerId));

        if (customerDTO.getEndereco() != null) {
            boolean isEnderecoAlterado = !(customer.getEndereco().equals(customerDTO.getEndereco()));
            if (isEnderecoAlterado) {
                customer.setEndereco(customerDTO.getEndereco());
            }
        }

        if (customerDTO.getEmail() != null) {
            boolean isEmailAlterado = !(customer.getEmail().equals(customerDTO.getEmail()));
            if (isEmailAlterado) {
                validEmailUnique(customerDTO.getEmail(), customerId);
                customer.setEmail(customerDTO.getEmail());
            }
        }

        if (customerDTO.getTelefone() != null) {
            boolean isTelefoneAlterado = !(customer.getTelefone().equals(customerDTO.getTelefone()));
            if (isTelefoneAlterado) {
                customer.setTelefone(customerDTO.getTelefone());
            }
        }

        if ((customerDTO.getInteresses() != null)) {
                Set<CategoryDTO> interesses = categoryService.validCategorys(customerDTO.getInteresses());
                customer.setInteresses(interesses
                        .stream()
                        .map(Category::convert)
                        .collect(Collectors.toSet())
                );
        }

        return CustomerDTO.convert(customerRepository.save(customer));
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

    public List<CustomerDTO> findByCategory(Long categoryId) {
        List<CustomerDTO> customers = findAll();
        CategoryDTO category = categoryService.findById(categoryId);
        return customers.stream().filter(customer -> customer.getInteresses().contains(category)).toList();
    }

    public Map<CategoryDTO, List<CustomerDTO>> getCustomersGroupingByCategory() {

        Map<CategoryDTO, List<CustomerDTO>> groupByCategory = new LinkedHashMap<>();

        List<CustomerDTO> customers = findAll();

        List<CategoryDTO> categories = categoryService.findAll()
                .stream()
                .sorted(Comparator.comparing(CategoryDTO::getNome))
                .toList();

        categories.forEach(category -> {

            List<CustomerDTO> customersByCategory = new ArrayList<>();

            customers.forEach(customer -> {
                if (customer.getInteresses().contains(category)) {
                    customersByCategory.add(customer);
                }
            });

            if (!customersByCategory.isEmpty()) {
                List<CustomerDTO> customersSorted = customersByCategory
                        .stream()
                        .sorted(Comparator.comparing(CustomerDTO::getNome))
                        .toList();
                groupByCategory.put(category, customersSorted);
            }
        });

        return groupByCategory;

    }

    @Transactional
    public AuthUserDTO createAuthUser(Long customerId, PasswordDTO password) {

        CustomerDTO customerDTO = findById(customerId);

        if (customerDTO.getKeyAuth() != null && !customerDTO.getKeyAuth().isEmpty()) {
            Optional<AuthUserDTO> authUserDTO = authService.findAuthUserByKeyToken(customerDTO.getKeyAuth());
            if (authUserDTO.isPresent()) {
                throw new CustomerAuthUserConflictException();
            }
        }

        if (!password.getPassword().equals(password.getRePassword())) {
            throw new AuthPasswordNotMatchException();
        }

        CreateAuthUserDTO createAuthUserDTO = new CreateAuthUserDTO();
        createAuthUserDTO.setNome(customerDTO.getNome());
        createAuthUserDTO.setPassword(password.getPassword());
        createAuthUserDTO.setUsername(customerDTO.getEmail());
        createAuthUserDTO.setEmail(customerDTO.getEmail());

        AuthUserDTO authUserDTO = authService.createAuthUser(createAuthUserDTO);

        customerDTO.setKeyAuth(authUserDTO.getKeyToken());
        Customer customer = Customer.convert(customerDTO);

        customerRepository.save(customer);

        return authUserDTO;

    }

    @Transactional
    public CreateCustomerUserDTO createCustomerAndAuthUser(CreateCustomerUserDTO createCustomerUserDTO) {
        CustomerDTO customerDTO = save(createCustomerUserDTO.getCustomer());
        createAuthUser(customerDTO.getId(), createCustomerUserDTO.getPassword());
        return createCustomerUserDTO;
    }

}
