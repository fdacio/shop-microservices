package br.com.daciosoftware.shop.auth.config;

import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.RuleDTO;
import br.com.daciosoftware.shop.models.dto.auth.TokenDTO;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.time.Instant;
import java.util.stream.Collectors;

@Configuration
public class TokenConfig {

    @Autowired
    RsaKey rsaKey;

    public JwtEncoder jwtEncoder() throws Exception {
        JWK jwk = new RSAKey.Builder(rsaKey.getPublicKey()).privateKey(rsaKey.getPrivate()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    public TokenDTO getToken(AuthUserDTO authUserDTO) {

        var now = Instant.now();
        long expire = 300L;

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
