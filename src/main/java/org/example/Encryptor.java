package org.example;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.UUID;

public class Encryptor {
    @Getter
    @Setter

    public String Hashed ;
    private String Pepper;

    private KeyStore ks ;
    private String Key_Password =System.getenv("KEYSTORE_PASSWORD");
    private String Path =System.getenv("KEYSTORE_PATH");

    public Encryptor(String password) {

      GetPepper();
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

    private void GetPepper()
    {
        try {
            //open the folder
            ks = KeyStore.getInstance("PKCS12");
            //Create InputStream
            FileInputStream fis = new FileInputStream(Path);
            //Load content from the file
            ks.load(fis,Key_Password.toCharArray());
            KeyStore.Entry entry = ks.getEntry("pepper",new KeyStore.PasswordProtection(Key_Password.toCharArray()));
            KeyStore.SecretKeyEntry secretKeyEntry=(KeyStore.SecretKeyEntry) entry;
            Pepper=new String(secretKeyEntry.getSecretKey().getEncoded());


        }
        catch (KeyStoreException e)
        {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UnrecoverableEntryException e) {
            System.out.println(e.getMessage());
        } catch (CertificateException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }

}
