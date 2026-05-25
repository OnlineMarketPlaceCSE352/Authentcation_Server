package org.example.Main;

import org.example.ChargeCredit.ChargeCreditHandler;
import org.example.DeleteUser.DeleteUserHandler;
import org.example.Enums.Status;
import org.example.Login.LoginHandler;
import org.example.Parser.Request;
import org.example.Parser.Response;
import org.example.Payment.PaymentHandler;
import org.example.Profile.ProfileHandler;
import org.example.PublicKey.PublicKeyHandler;
import org.example.SearchUser.SearchUserHandler;
import org.example.SignUp.SignUpHandler;
import org.example.Tokniz.JWTRSA256;
import org.example.Withdraw.WithdrawHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        JWTRSA256 keys = new JWTRSA256();
        try {
            ServerSocket Server = new ServerSocket(8081);
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                System.out.println("[MAIN THREAD] Waiting for new connection...");

                Socket client = Server.accept();
                System.out.println(
                        "[MAIN THREAD] Client Connected"
                );
                pool.execute(() -> {
                    System.out.println(
                            "[WORKER] Handling Client: "
                                    + client.getInetAddress()
                    );
                    handleClient(client, keys);
                });

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClient(Socket Client, JWTRSA256 keys) {
        try {

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
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

// Read body
            char[] bodyChars = new char[contentLength];
            IS.read(bodyChars, 0, contentLength);

            String body = new String(bodyChars);

            String fullRequest = headers + "\n" + body;

            System.out.println("Accept new Message" + "\n" + fullRequest);


            if (fullRequest.toString().trim().isEmpty()) {
                System.out.println("Received empty ping. Ignoring.");
                Client.close();

                return;
            }

            Request request = new Request(fullRequest.toString());
//!Public key request
            if (request.EndPoint.equals("/api/auth/key_req")) {
                System.out.println("Accept Public_Key_req");
                PublicKeyHandler p = new PublicKeyHandler(request, keys);
                p.Handle();
                System.out.println("Sending Public Key");
                System.out.println(p.getResponse().ToString());
                OS.print(p.getResponse().ToString());
                OS.flush();
                Client.close();
            }
            //!Signup request
            else if (request.EndPoint.equals("/api/auth/register")) {
                System.out.println("Accepted user sign Up request");
                SignUpHandler s = new SignUpHandler(request, keys);
                System.out.println(s.getResponse().ToString());
                OS.print(s.getResponse().ToString());
                OS.flush();
                Client.close();
            } else if (request.EndPoint.equals("/api/auth/payment")) {
                System.out.println("Accepted user payment request");
                PaymentHandler py = new PaymentHandler(request, keys);
                System.out.println(py.getResponse().ToString());
                OS.print(py.getResponse().ToString());
                OS.flush();
                Client.close();
            } else if (request.EndPoint.equals("/api/auth/login")) {
                System.out.println("Accepted user login");
                LoginHandler py = new LoginHandler(request, keys);
                System.out.println(py.getResponse().ToString());
                OS.print(py.getResponse().ToString());
                OS.flush();
                Client.close();
            } else if (request.EndPoint.equals("/api/auth/profile")) {
                System.out.println("Accepted user Profile");
                ProfileHandler Pf = new ProfileHandler(request, keys);
                System.out.println(Pf.getResponse().ToString());
                OS.print(Pf.getResponse().ToString());
                OS.flush();
                Client.close();
            } else if (request.EndPoint.equals("/api/auth/profile/charge")) {
                System.out.println("Accepted user charge");
                ChargeCreditHandler ch = new ChargeCreditHandler(request, keys);
                System.out.println(ch.getResponse().ToString());
                OS.print(ch.getResponse().ToString());
                OS.flush();
                Client.close();
            } else if (request.EndPoint.equals("/api/auth/profile/withdraw")) {
                System.out.println("Accepted user withdraw");
                WithdrawHandler wd = new WithdrawHandler(request, keys);
                System.out.println(wd.getResponse().ToString());
                OS.print(wd.getResponse().ToString());
                OS.flush();
                Client.close();
            } else if (request.EndPoint.equals("/api/auth/profile/search")) {
                System.out.println("Accepted user search");
                SearchUserHandler us = new SearchUserHandler(request, keys);
                System.out.println(us.getResponse().ToString());
                OS.print(us.getResponse().ToString());
                OS.flush();
                Client.close();
            }else if (request.EndPoint.equals("/api/auth/profile/delete")) {
                System.out.println("Accepted Admin Delete");
                DeleteUserHandler us = new DeleteUserHandler(request, keys);
                System.out.println(us.getResponse().ToString());
                OS.print(us.getResponse().ToString());
                OS.flush();
                Client.close();
            } else {
                System.out.println("BadEndPoint");

                Response response = new Response();
                response.Body.put("message", "No Such GETWAY");
                response.setStauts(Status.BAD_GATEWAY);
                OS.print(response);
                OS.flush();
                Client.close();
            }

        } catch (Exception e) {

            e.printStackTrace();

            try {
                Client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
