package br.com.daciosoftware.shop.auth.config;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKey {

    public RSAPublicKey getPublicKey() throws Exception {

        URL urlFilePublicKey = this.getClass().getClassLoader().getResource("keys/app.public.key");
        if (urlFilePublicKey == null) {
            throw new Exception("Não há arquivo de chave publica");
        }

        File file = new File(urlFilePublicKey.getFile());
        Path pathToFile = Paths.get(file.toURI());
        String key = Files.readString(pathToFile, Charset.defaultCharset());

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public RSAPrivateKey getPrivate() throws Exception {

        URL urlFilePublicKeY = this.getClass().getClassLoader().getResource("keys/app.private.key");
        if (urlFilePublicKeY == null) {
            throw new Exception("Não há arquivo de chave privada");
        }

        File file = new File(urlFilePublicKeY.getFile());
        Path pathToFile = Paths.get(file.toURI());
        String key = Files.readString(pathToFile, Charset.defaultCharset());
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
