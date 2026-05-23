package org.example;

import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.json.JSONObject;

public class PaymentHandler {
    Request request;
    Response response;
    JWTRSA256 keys;
    User Seller;
    User Costumer;

    PaymentHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();

    }

    public void Handle() throws ApiException {

        if (request.method.equals(Methods.POST)) {
            //! checking the Token
            try {
                JWTRSA256.Authencator(request.getHeader().getString("token"), keys.getPublicKey().toString());
                PaymentServices.Payment(request.getHeader().getString("token"),request.getBody().getString("userId"))
            } catch (ApiException ex) {
                response.setStauts(ex.getStatus());
                JSONObject body = new JSONObject();
                body.put("message", ex.getMessage());
                response.setBody(body);


            }


        } else {
            JSONObject Body = new JSONObject();
            Body.put("message", "Expecting GET on these EndPoint");
            response.stauts = Status.METHOD_NOT_ALLOWED;
            response.Body = Body;

        }

    }
}


