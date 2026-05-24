package org.example.Payment;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.example.Exceptions.ApiException;
import org.example.Parser.Request;
import org.example.Parser.Response;
import org.example.Tokniz.JWTRSA256;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
@Getter
@Setter
public class PaymentHandler {
    Request request;
     Response response;
    JWTRSA256 keys;


    public PaymentHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();

    }

    public void Handle() throws ApiException {

        if (request.method.equals(Methods.POST)) {
            //! checking the Token
            try {

                JWTRSA256.Authencator(request,  Base64.getEncoder()
                        .encodeToString(keys.getPublicKey().getEncoded()));
                String token;
                String sellerId;
                String userId;
                double amount;
                    token = request.getHeader().getString("token");
                    String[] chunks = token.split("\\.");
                    Base64.Decoder decoder = Base64.getUrlDecoder();

                    String header = new String(decoder.decode(chunks[0]));
                    String payload = new String(decoder.decode(chunks[1]));
                    JSONObject obj = new JSONObject(payload);
                    userId = obj.getString("id");

                try{
                    sellerId =request.getBody().getString("userId");

                }

                catch (JSONException E)
                {
                    throw new ApiException(Status.BAD_REQUEST, "Missing userId ");


                }
                catch (NullPointerException ex)
                {throw new ApiException(Status.BAD_REQUEST, "Missing Body ");}
                try{
                    amount =request.getBody().getDouble("amount");

                }

                catch (JSONException E)
                {
                    throw new ApiException(Status.BAD_REQUEST, "Missing amount ");


                }


               double c= PaymentServices.Payment(sellerId,userId,amount);
                response.setStauts(Status.ACCEPTED);
                response.Body.put("credit",c);

            } catch (ApiException ex) {
                response.setStauts(ex.getStatus());
                JSONObject body = new JSONObject();
                body.put("message", ex.getMessage());
                response.setBody(body);


            }


        } else {
            JSONObject Body = new JSONObject();
            Body.put("message", "Expecting GET on these EndPoint");
            response.setStauts(Status.METHOD_NOT_ALLOWED);
            response.Body = Body;

        }

    }
}


