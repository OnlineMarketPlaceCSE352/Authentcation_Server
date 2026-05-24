package org.example.Encryptor;

import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Base64;

public class Encryptor {
    @Getter
    @Setter

    public String Hashed ;
    private static String Pepper;

    private KeyStore ks ;
    private String Key_Password =System.getenv("KEYSTORE_PASSWORD");
    private String Path =System.getenv("KEYSTORE_PATH");

    public Encryptor(String password) {


       Hashed = BCrypt.hashpw(password+Pepper,BCrypt.gensalt(12));
    }
    public String Encrypt(String password)
    {
        Hashed = BCrypt.hashpw(password+Pepper,BCrypt.gensalt(12));
        return  Hashed  ;
    }

    public boolean CheckPass(String password)
    {
        return BCrypt.checkpw(password+Pepper,this.Hashed);

    }
    public static boolean Check2Pass(String plainPassword,String hashedPassword)
    {
        System.out.println("Plain Text: "+plainPassword);
        System.out.println("Hashed text: "+hashedPassword);
        return BCrypt.checkpw(plainPassword+Pepper,hashedPassword);
    }



    static {
        LoadPepper();
        if (Pepper == null) {
            throw new RuntimeException("Pepper not loaded");
        }
    }

    private static void LoadPepper() {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");

            FileInputStream fis =
                    new FileInputStream(System.getenv("KEYSTORE_PATH"));

            String keyPassword = System.getenv("KEYSTORE_PASSWORD");

            ks.load(fis, keyPassword.toCharArray());

            KeyStore.Entry entry =
                    ks.getEntry(
                            "pepper",
                            new KeyStore.PasswordProtection(
                                    keyPassword.toCharArray()
                            )
                    );

            KeyStore.SecretKeyEntry secretKeyEntry =
                    (KeyStore.SecretKeyEntry) entry;


            Pepper = Base64.getEncoder()
                    .encodeToString(
                            secretKeyEntry.getSecretKey().getEncoded()
                    );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
