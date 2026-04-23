package org.example;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
@Getter
public class JWTRSA256 {
    private  String Password =System.getenv("KEYSTORE_PASSWORD");
    private String Path =System.getenv("KEYSTORE_PATH");
   private RSAPublicKey publicKey;
   private RSAPrivateKey privateKey;

    JWTRSA256(){
        KeyStore ks=null;
        try {
             ks =KeyStore.getInstance("PKCS12");

            FileInputStream fis = new FileInputStream(Path);
            ks.load(fis,Password.toCharArray());
            privateKey =(RSAPrivateKey) ks.getKey("jwt-key",Password.toCharArray());
            System.out.println(privateKey);
            publicKey=(RSAPublicKey) ks.getCertificate("jwt-key").getPublicKey();
        }

        catch(Exception e){
            System.out.println(e.getMessage());
        }




    }

    public String Genrate(HashMap<String,String> payload)
    {
        Builder tokenBuilder= JWT.create();
        tokenBuilder.withClaim("jti", UUID.randomUUID().toString());
      Calendar  c=Calendar.getInstance();
        tokenBuilder.withIssuedAt(c.getTime());
        c.add(Calendar.MINUTE, 30);
        tokenBuilder.withExpiresAt(c.getTime());
        tokenBuilder.withClaim("token_type", "Bearer");
        payload.entrySet().forEach(action->tokenBuilder.withClaim(action.getKey(), action.getValue()));
        return tokenBuilder.sign(Algorithm.RSA256(publicKey,privateKey));
    }
}
