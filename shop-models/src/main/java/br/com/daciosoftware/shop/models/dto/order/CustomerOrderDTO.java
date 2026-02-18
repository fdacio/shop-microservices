package br.com.daciosoftware.shop.models.dto.order;

import br.com.daciosoftware.shop.models.entity.customer.Customer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerOrderDTO {
    private String nome;
    private String cpf;
    private String endereco;
    private String email;
    private String telefone;
    public static CustomerOrderDTO convert(Customer customer) {
        CustomerOrderDTO customerOrderDTO = new CustomerOrderDTO();
        customerOrderDTO.setNome(customer.getNome());
        customerOrderDTO.setCpf(customer.getCpf());
        customerOrderDTO.setEndereco(customer.getEndereco());
        customerOrderDTO.setEmail(customer.getEmail());
        customerOrderDTO.setTelefone(customer.getTelefone());
        return customerOrderDTO;
    }
}
