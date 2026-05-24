package org.example.Tokniz;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.example.Exceptions.ApiException;
import org.example.Enums.Status;
import org.example.Parser.Request;
import org.example.User.User;
import org.json.JSONException;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

@Getter
public class JWTRSA256 {
    private String Password = System.getenv("KEYSTORE_PASSWORD");
    private String Path = System.getenv("KEYSTORE_PATH");
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public JWTRSA256() {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("PKCS12");

            FileInputStream fis = new FileInputStream(Path);
            ks.load(fis, Password.toCharArray());
            privateKey = (RSAPrivateKey) ks.getKey("jwt-key", Password.toCharArray());
            System.out.println(privateKey);
            publicKey = (RSAPublicKey) ks.getCertificate("jwt-key").getPublicKey();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static void Authencator(Request request, String publicKey) {

        /* Verification of JWT */
        System.out.println("PublicKey: "+publicKey);
        System.out.println("Token: "+request.getHeader().get("token"));


        try {
            String token = request.getHeader().getString("token");
            /// refine the publickey
            publicKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
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


        }catch(TokenExpiredException ex){
            throw new ApiException(Status.GATEWAY_TIMEOUT, "Token has expired");
        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException | JWTVerificationException e) {

            throw new ApiException(Status.UNAUTHORIZED, "Token is Manipulated");
        } catch (JSONException ex) {
            throw new ApiException(Status.BAD_REQUEST, "Missing Token");

        }

    }

    public String Genrate(User user) {
        Builder tokenBuilder = JWT.create();
        tokenBuilder.withClaim("jti", UUID.randomUUID().toString());
        Calendar c = Calendar.getInstance();
        tokenBuilder.withIssuedAt(c.getTime());
        c.add(Calendar.MINUTE, 30);
        tokenBuilder.withExpiresAt(c.getTime());
        tokenBuilder.withClaim("id", user.getId());
        tokenBuilder.withClaim("role", user.getRole().name());
        tokenBuilder.withClaim("email", user.getEmail());
        return tokenBuilder.sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
