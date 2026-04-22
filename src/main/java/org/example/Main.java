package org.example;
import org.example.Enums.Methods;
import org.json.JSONObject;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.net.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      /*
        try {
            ServerSocket Server = new ServerSocket(8081);
            Socket Client = Server.accept();
            DataInputStream IS = new DataInputStream(Client.getInputStream());
            DataOutputStream OS = new DataOutputStream(Client.getOutputStream());
            String request = IS.readUTF();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/

        Request r =new Request("""
                
                          POST /login
                          [CRLF]
                           {
                            "email": "ewkw@gmail",
                            "password": "123"
                            }
                             """);

       JSONObject j = r.GetBody();
       Methods method = r.GetMethod();
       System.out.println(j);
       System.out.println(method);

    }

    }
