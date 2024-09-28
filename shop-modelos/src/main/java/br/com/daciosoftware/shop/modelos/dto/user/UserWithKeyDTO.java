package br.com.daciosoftware.shop.modelos.dto.user;

import br.com.daciosoftware.shop.modelos.dto.product.CategoryDTO;
import br.com.daciosoftware.shop.modelos.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserWithKeyDTO  {

    private Long id;
    private String nome;
    private String cpf;
    private String endereco;
    private String email;
    private String telefone;
    private LocalDateTime dataCadastro;
    private Set<CategoryDTO> interesses;
    private String key;

    public static UserWithKeyDTO convert(User user) {
        UserWithKeyDTO userWithKeyDTO = new UserWithKeyDTO();
        userWithKeyDTO.setKey(user.getKey());
        userWithKeyDTO.setId(user.getId());
        userWithKeyDTO.setNome(user.getNome());
        userWithKeyDTO.setCpf(user.getCpf());
        userWithKeyDTO.setTelefone(user.getTelefone());
        userWithKeyDTO.setEmail(user.getEmail());
        userWithKeyDTO.setEndereco(user.getEndereco());
        userWithKeyDTO.setDataCadastro(user.getDataCadastro());
        if (user.getInteresses() != null)
            userWithKeyDTO.setInteresses(user.getInteresses().stream().map(CategoryDTO::convert).collect(Collectors.toSet()));
        return userWithKeyDTO;
    }
}
