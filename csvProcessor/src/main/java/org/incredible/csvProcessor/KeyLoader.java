package org.incredible.csvProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyLoader {

    public static KeyPair load(String algorithm, String publicKeyFile, String privateKeyFile) {
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            publicKey = loadPublicKey(algorithm, publicKeyFile);
            privateKey = loadPrivateKey(algorithm, privateKeyFile);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new KeyPair(publicKey, privateKey);
    }

    public static PrivateKey loadPrivateKey(String algorithm, String privateKeyFile)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = readFile(privateKeyFile);

        /* Generate private key. */
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        PrivateKey pvt = kf.generatePrivate(ks);
        return pvt;
    }

    public static PublicKey loadPublicKey(String algorithm, String publicKeyFile)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = readFile(publicKeyFile);

        /* Generate public key. */
        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(ks);
        return publicKey;
    }

    private static byte[] readFile(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.readAllBytes(path);
    }
}
