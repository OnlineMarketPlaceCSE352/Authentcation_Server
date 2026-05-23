package org.example;

import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.json.JSONObject;

import java.util.Base64;

public class PublicKeyHandler {
    Request request;
    JWTRSA256 keys;
    Response response ;

    PublicKeyHandler(Request r,JWTRSA256 keys)
    {
        this.request=r;
        this.keys=keys;
        response=new Response();
    }
   public void Handle() {
        if (request.method.equals(Methods.GET)) {
            JSONObject Body = new JSONObject();
            Body.put("publicKey", Base64.getEncoder()
                    .encodeToString(keys.getPublicKey().getEncoded()));
            response.stauts = Status.ACCEPTED;
            response.Body = Body;


        } else {
            JSONObject Body = new JSONObject();
            Body.put("message", "Expecting GET on these EndPoint");
            response.stauts = Status.METHOD_NOT_ALLOWED;
            response.Body = Body;


        }

    }

}
