package org.example;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.example.Enums.Status;
import org.json.JSONObject;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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

    public String Genrate(User user)
    {
        Builder tokenBuilder= JWT.create();
        tokenBuilder.withClaim("jti", UUID.randomUUID().toString());
      Calendar  c=Calendar.getInstance();
        tokenBuilder.withIssuedAt(c.getTime());
        c.add(Calendar.MINUTE, 30);
        tokenBuilder.withExpiresAt(c.getTime());
        tokenBuilder.withClaim("id", user.getId());
        tokenBuilder.withClaim("role", user.getRole().name());
        tokenBuilder.withClaim("email", user.getEmail());
        return tokenBuilder.sign(Algorithm.RSA256(publicKey,privateKey));
    }

    public static void Authencator(String token, String publicKey) {


        /* Verification of JWT */
        try {


            //Convert public key string to RSAPublicKey
            byte[] publicKeyByteArr = Base64.getDecoder().decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteArr));

            //If the token has an invalid signature, JWTVerificationException will raise.
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | JWTVerificationException e) {

            throw new ApiException(Status.UNAUTHORIZED, "Token is Manipulated");
        }

    }
}
