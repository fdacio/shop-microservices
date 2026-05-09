package br.com.daciosoftware.shop.models.dto.customer;

import br.com.daciosoftware.shop.models.entity.customer.Customer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerShotDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String endereco;
    private String email;
    private String telefone;
    public static CustomerShotDTO convert(Customer customer) {
        CustomerShotDTO dto = new CustomerShotDTO();
        dto.setId(customer.getId());
        dto.setNome(customer.getNome());
        dto.setCpf(customer.getCpf());
        dto.setEndereco(customer.getEndereco());
        dto.setEmail(customer.getEmail());
        dto.setTelefone(customer.getTelefone());
        return dto;
    }
    public static CustomerShotDTO convert(CustomerDTO customer) {
        CustomerShotDTO dto = new CustomerShotDTO();
        dto.setId(customer.getId());
        dto.setNome(customer.getNome());
        dto.setCpf(customer.getCpf());
        dto.setEndereco(customer.getEndereco());
        dto.setEmail(customer.getEmail());
        dto.setTelefone(customer.getTelefone());
        return dto;
    }
}
