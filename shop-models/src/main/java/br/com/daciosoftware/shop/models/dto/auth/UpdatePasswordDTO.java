package br.com.daciosoftware.shop.models.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePasswordDTO {

    private String newPassword;
    private String rePassword;
}
