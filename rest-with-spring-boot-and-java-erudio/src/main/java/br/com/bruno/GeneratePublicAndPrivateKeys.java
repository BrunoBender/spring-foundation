package br.com.bruno;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GeneratePublicAndPrivateKeys {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Gerar um par de chaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Chave privada no formato PKCS#8
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        System.out.println("-----BEGIN PRIVATE KEY-----");
        System.out.println(privateKey);
        System.out.println("-----END PRIVATE KEY-----");

        // Chave pública
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(publicKey);
        System.out.println("-----END PUBLIC KEY-----");
    }
}
