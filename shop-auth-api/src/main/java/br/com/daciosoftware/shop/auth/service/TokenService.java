package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.models.dto.auth.*;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final ConfigService configService;

    public JwtEncoder jwtEncoder() throws Exception {
        RsaKey rsaKey = new RsaKey();
        JWK jwk = new RSAKey.Builder(rsaKey.getRsaPublicKey()).privateKey(rsaKey.getRsaPrivateKey()).build();
        var immutableJwk = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(immutableJwk);
    }

    public TokenDTO getToken(AuthUserDTO authUserDTO) {

        Instant now = Instant.now();
        Optional<ConfigDTO> optionalConfigDTO = configService.findOptionalByChave(ConfigEnum.EXPIRE_TOKEN.getChave());
        long expire = optionalConfigDTO.map(configDTO -> Long.parseLong(configDTO.getValor())).orElse(300L);

        String scopes = authUserDTO.getRules()
                .stream()
                .map(RuleDTO::getNome)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("shop-api")
                .subject(authUserDTO.getKeyToken())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expire))
                .claim("scope", scopes)
                .build();

        try {
            String tokenValue = jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(tokenValue);
            tokenDTO.setExpireToken(expire);
            return tokenDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
