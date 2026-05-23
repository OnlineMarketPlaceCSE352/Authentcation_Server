package org.example;

import org.example.Enums.UserRepository;
import org.example.Enums.VisaRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        JWTRSA256 keys = new JWTRSA256();
        try {
            ServerSocket Server = new ServerSocket(8081);
            while (true) {
                System.out.println("=====Waiting for new Connection=====");
                Socket Client = Server.accept();
                System.out.println("Accept new request");
//            DataInputStream IS = new DataInputStream(Client.getInputStream());
//            DataOutputStream OS = new DataOutputStream(Client.getOutputStream());


                PrintWriter OS = new PrintWriter(Client.getOutputStream(), false);

                BufferedReader IS = new BufferedReader(new InputStreamReader(Client.getInputStream()));

                String line;
                StringBuilder headers = new StringBuilder();

                int contentLength = 0;

// Read headers
                while ((line = IS.readLine()) != null && !line.isEmpty()) {

                    headers.append(line).append("\n");

                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(
                                line.split(":")[1].trim()
                        );
                    }
                }

// Read body
                char[] bodyChars = new char[contentLength];
                IS.read(bodyChars, 0, contentLength);

                String body = new String(bodyChars);

                String fullRequest = headers + "\n" + body;

                System.out.println("Accept new Message" +"\n"+fullRequest);


                if (fullRequest.toString().trim().isEmpty()) {
                    System.out.println("Received empty ping. Ignoring.");
                    Client.close();
                    continue;
                }

                Request request = new Request(fullRequest.toString());
//!Public key request
                if (request.EndPoint.equals("/api/auth/key_req")) {
                    System.out.println("Accept Public_Key_req");
                    PublicKeyHandler p = new PublicKeyHandler(request, keys);
                    p.Handle();
                    System.out.println("Sending Public Key");
                    System.out.println(p.response.ToString());
                    OS.print(p.response.ToString());
                    OS.flush();
                    Client.close();
                }
                //!Signup request
                else if (request.EndPoint.equals("/api/auth/register")) {
                    System.out.println("Accepted user sign Up request");
                    SignUpHandler s = new SignUpHandler(request, keys);
                    System.out.println(s.response.ToString());
                    OS.print(s.response.ToString());
                    OS.flush();
                    Client.close();
                } else if (request.EndPoint.equals("/api/auth/payment")) {
                    System.out.println("Accepted user payment request");
                    SignUpHandler s = new SignUpHandler(request, keys);
                    System.out.println(s.response.ToString());
                    OS.print(s.response.ToString());
                    OS.flush();
                    Client.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

//  ///Testing Encryptor
/// /        Encryptor e= new Encryptor("Ahmed");
/// /        System.out.println(e.getHashed());
/// /        System.out.println(e.CheckPass("Ahmed"));
// //Testing Request and Response Class
//        Request r =new Request("""
//                    GET /api/auth/key_req
//                    {}
//                    [CRLF]
//                    {
//                            message="asdsafafk";
//                    }
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
/// /        res.getReponse();
//
//       System.out.println("Body"+ j);
//       System.out.println("Head"+k);
//       System.out.println("Method"+method);
//       System.out.println("EndPoint"+s);
/// /       System.out.println("response"+res.Response);

//        Request R = new Request("POST /abi/save HTTP/1.1\n" +
//                "User-Agent: Chrome\n" +
//                "Content-Type: application/json\n" +
//                "\n" +
//                "{\"name\":\"Ahmed\",\"id\":2}");
//
//
//
//    }
//
//}
