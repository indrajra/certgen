package org.incredible.csvProcessor;

import org.bouncycastle.openssl.PEMWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyWriter {
    public void writePrivateKey(PrivateKey pvt, String outDir, String name) throws IOException {
        FileOutputStream out = new FileOutputStream(outDir + name +".key");
        out.write(pvt.getEncoded());
        out.close();
    }

    public void write(KeyPair keyPair, String outDir) throws IOException {
        writePrivateKey(keyPair.getPrivate(), outDir, "private");

        writePublicKey(keyPair.getPublic(), outDir, "public");
        writePublicKeyAsPem(keyPair.getPublic(), outDir, "public");
    }

    public void writePublicKey(PublicKey publicKey, String outDir, String name) throws IOException {
        FileOutputStream out = new FileOutputStream(outDir + name + ".pub");
        out.write(publicKey.getEncoded());
        out.close();
    }

    public String getPublicKey(PublicKey publicKey) throws IOException {
        StringWriter publicWrite = new StringWriter();
        PEMWriter publicPemWriter = new PEMWriter(publicWrite);
        publicPemWriter.writeObject(publicKey);
        publicPemWriter.close();
        publicWrite.close();

        return publicWrite.toString();
    }

    public void writePublicKeyAsPem(PublicKey publicKey, String outDir, String name) throws IOException {
        FileOutputStream out2 = new FileOutputStream(outDir + name + ".pem");
        out2.write(getPublicKey(publicKey).getBytes());
        out2.close();
    }
}
