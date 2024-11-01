package br.com.daciosoftware.shop.auth.keys.component;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class RsaKey {

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String getPrivateKeyPEM () {
        ClassPathResource resource = new ClassPathResource("keys/app.private.key");
        String key = asString(resource);
        return  key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
    }

    private String getPublicKeyPEM () {
        ClassPathResource resource = new ClassPathResource("keys/app.public.key");
        String key = asString(resource);
        return  key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");
    }

    public PublicKeyDTO getPublicKeyDTO() {
        String publicKeyPEM = getPublicKeyPEM();
        PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
        publicKeyDTO.setContent(publicKeyPEM);
        return publicKeyDTO;
    }

    public PrivateKeyDTO getPrivateKeyDTO() {
        String privateKeyPEM = getPrivateKeyPEM();
        PrivateKeyDTO privateKeyDTO = new PrivateKeyDTO();
        privateKeyDTO.setContent(privateKeyPEM);
        return privateKeyDTO;
    }

    @Bean
    public RSAPublicKey getRsaPublicKey() throws Exception {
        String publicKeyPEM = getPublicKeyPEM();
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @Bean
    public RSAPrivateKey getRsaPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = getPrivateKeyPEM();
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
