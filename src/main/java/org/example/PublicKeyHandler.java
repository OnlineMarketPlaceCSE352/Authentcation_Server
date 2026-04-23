package org.example;

import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.json.JSONObject;

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
            Body.append("PublicKey", keys.getPublicKey());
            response.stauts = Status.ACCEPTED;
            response.Body = Body;


        } else {
            JSONObject Body = new JSONObject();
            Body.append("Message", "Expecting GET on these EndPoint");
            response.stauts = Status.METHOD_NOT_ALLOWED;
            response.Body = Body;


        }

    }

}
