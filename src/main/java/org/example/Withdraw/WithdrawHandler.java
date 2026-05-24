package org.example.Withdraw;

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
public class WithdrawHandler {
    Request request;
    Response response;
    JWTRSA256 keys;


    public WithdrawHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();
    }
    public void Handle() throws ApiException
        {
            if (request.method.equals(Methods.PUT)) {
                try {

                    JWTRSA256.Authencator(request, Base64.getEncoder().encodeToString(keys.getPublicKey().getEncoded()));
                    String userId;
                    String token = request.getHeader().getString("token");
                    String[] chunks = token.split("\\.");
                    Base64.Decoder decoder = Base64.getUrlDecoder();
                    String payload = new String(decoder.decode(chunks[1]));
                    JSONObject obj = new JSONObject(payload);
                    userId = obj.getString("id");

                    //!trying fetch the userId
                    double amount;
                    String OTP;
                    try {
                        amount = request.getBody().getDouble("amount");

                    } catch (JSONException ex) {
                        throw new ApiException(Status.BAD_REQUEST, "Missing Amount");
                    } catch (NullPointerException e) {
                        throw new ApiException(Status.BAD_REQUEST, "Missing Body");
                    }
                    double newAmount = WithdrawServices.Charge(userId, amount);
                    response.setStauts(Status.ACCEPTED);
                    response.getBody().put("newCredit", newAmount);


                } catch (ApiException ex) {
                    response.setStauts(ex.getStatus());
                    JSONObject body = new JSONObject();
                    body.put("message", ex.getMessage());
                    response.setBody(body);
                }


            } else {
                JSONObject Body = new JSONObject();
                Body.put("message", "Expecting PUT on these EndPoint");
                response.setStauts(Status.METHOD_NOT_ALLOWED);
                response.Body = Body;

            }
        }
    }

