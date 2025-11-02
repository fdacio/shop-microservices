package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.customer.repository.CustomerRepository;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthPasswordNotMatchException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.*;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.PasswordDTO;
import br.com.daciosoftware.shop.models.dto.customer.CreateCustomerAndAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.models.entity.customer.Customer;
import br.com.daciosoftware.shop.models.entity.product.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuthService authService;

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

    @Transactional(rollbackFor = RuntimeException.class)
    public CustomerDTO save(CustomerDTO customerDTO) {

        validCpfUnique(customerDTO.getCpf());
        validEmailUnique(customerDTO.getEmail(), null);
        validInteresses(customerDTO.getInteresses());

        customerDTO.setDataCadastro(LocalDateTime.now());

        Customer customer = Customer.convert(customerDTO);

        return CustomerDTO.convert(customerRepository.save(customer));
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthUserDTO createAuthUserFromCustomer(Long customerId, PasswordDTO password) {

        CustomerDTO customerDTO = findById(customerId);

        validPassword(password);
        validKeyAuth(customerDTO.getKeyAuth());

        //Create AuthUser
        AuthUserDTO authUserDTO = createAuthUser(customerDTO, password);

        //Update KeyAuth in Customer
        customerDTO.setKeyAuth(authUserDTO.getKeyToken());
        Customer customer = Customer.convert(customerDTO);
        customerRepository.save(customer);

        return authUserDTO;

    }

    @Transactional(rollbackFor = RuntimeException.class)
    public CustomerDTO createCustomerAndAuthUser(CreateCustomerAndAuthUserDTO createCustomerAndAuthUserDTO) {

        //Create AuthUser
        CustomerDTO customerDTO = createCustomerAndAuthUserDTO.getCustomer();
        PasswordDTO passwordDTO = createCustomerAndAuthUserDTO.getPassword();

        //Verifica se senha e reSenha são iguais
        validPassword(passwordDTO);

        AuthUserDTO authUserDTO = createAuthUser(customerDTO, passwordDTO);

        //Create Customer and update KeyAuth
        String keyAuth = authUserDTO.getKeyToken();
        customerDTO.setKeyAuth(keyAuth);
        customerDTO = save(customerDTO);
        createCustomerAndAuthUserDTO.setCustomer(customerDTO);

        return customerDTO;
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

    public void delete(Long customerId) {
        CustomerDTO customerDTO = findById(customerId);
        Optional<String> keyAuthOptional = Optional.ofNullable(customerDTO.getKeyAuth());
        keyAuthOptional.ifPresent(this::validKeyAuth);
        customerRepository.delete(Customer.convert(customerDTO));
    }

    public void deleteCustomerAndAuthUser(Long customerId) {

        CustomerDTO customerDTO = findById(customerId);
        Optional<String> keyAuthOptional = Optional.ofNullable(customerDTO.getKeyAuth());

        try {
            //Delete Customer
            customerRepository.delete(Customer.convert(customerDTO));

            //Try delete AuthUser from Customer
            keyAuthOptional.ifPresent((keyAuth) -> {
                Optional<AuthUserDTO> authUserDTOOptional = authService.findAuthUserByKeyToken(keyAuth);
                authUserDTOOptional.ifPresent((authUserDTO -> authService.deleteAuthUser(authUserDTO)));
            });

        } catch (RuntimeException exception) {
            throw new CustomerIntegrityViolationException();
        }
    }

    public Page<CustomerDTO> findAllPageable(Pageable page) {
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
                .toStream()
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


    /*** Private Methods */

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

    private void validInteresses(Set<CategoryDTO> interesses) {
        categoryService.validCategorys(interesses);
    }

    private void validPassword(PasswordDTO password) {
        if (!password.getPassword().equals(password.getRePassword())) {
            throw new AuthPasswordNotMatchException();
        }
    }

    private void validKeyAuth(String keyAuth) {
        Optional<AuthUserDTO> authUserDTOOptional = authService.findAuthUserByKeyToken(keyAuth);
        if (authUserDTOOptional.isPresent()) throw new CustomerAuthUserConflictException();
    }

    private AuthUserDTO createAuthUser(CustomerDTO customerDTO, PasswordDTO password) {
        CreateAuthUserDTO createAuthUserDTO = new CreateAuthUserDTO();
        createAuthUserDTO.setNome(customerDTO.getNome());
        createAuthUserDTO.setUsername(customerDTO.getEmail());
        createAuthUserDTO.setEmail(customerDTO.getEmail());
        createAuthUserDTO.setPassword(password);
        return authService.createAuthUser(createAuthUserDTO);
    }

}
