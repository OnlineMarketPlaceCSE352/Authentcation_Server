package org.example;
import org.example.Enums.Methods;
import org.example.Enums.Status;
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

        JWTRSA256 keys =new JWTRSA256();
        try {
            ServerSocket Server = new ServerSocket(8081);
            Socket Client = Server.accept();
            System.out.println("Accept new request");
            DataInputStream IS = new DataInputStream(Client.getInputStream());
            DataOutputStream OS = new DataOutputStream(Client.getOutputStream());
            String message = IS.readUTF();
            System.out.println("Accept new Message"+message);

            Request request = new Request(message);

            if(request.EndPoint=="api/auth/key_req");
            System.out.println("Accept Public_Key_req");
            PublicKeyHandler p =new PublicKeyHandler(request,keys);
            p.Handle();
            System.out.println("Sending Public Key");
            System.out.println(p.response.ToString());

            OS.writeUTF(p.response.ToString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//  ///Testing Encryptor
////        Encryptor e= new Encryptor("Ahmed");
////        System.out.println(e.getHashed());
////        System.out.println(e.CheckPass("Ahmed"));
// //Testing Request and Response Class
//        Request r =new Request("""
//                    GET /api/auth/key_req
//                    {}
//                    [CRLF]
//                    {}
//                    """);
//
//       JSONObject j = r.getBody();
//       JSONObject k = r.getHeader();
//       Methods method = r.getMethod();
//       String s =r.getEndPoint();
//       Response res= new Response();
//       res.stauts= Status.ACCEPTED;
//        res.Header=k;
//        res.Body=j;
//        res.message="asdsad";
////        res.getReponse();
//
//       System.out.println("Body"+ j);
//       System.out.println("Head"+k);
//       System.out.println("Method"+method);
//       System.out.println("EndPoint"+s);
////       System.out.println("response"+res.Response);

    }

    }
