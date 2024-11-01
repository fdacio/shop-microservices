package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.auth.keys.component.RsaKey;
import br.com.daciosoftware.shop.models.dto.auth.*;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private ConfigService configService;

    public JwtEncoder jwtEncoder() {
        try {
            RsaKey rsaKey = new RsaKey();
            JWK jwk = new RSAKey.Builder(rsaKey.getRsaPublicKey()).privateKey(rsaKey.getRsaPrivateKey()).build();
            var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
            return new NimbusJwtEncoder(jwks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public TokenDTO getToken(AuthUserDTO authUserDTO) {

        var now = Instant.now();
        long expire;

        try {
            ConfigDTO configDTO = configService.findByChave(ConfigEnum.EXPIRE_TOKEN.getChave());
            expire = Long.parseLong(configDTO.getValor());
        } catch (Exception e){
            expire = 300L;
        }

        String scopes = authUserDTO.getRules()
                .stream()
                .map(RuleDTO::getNome)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
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
