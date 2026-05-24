package org.example.Profile;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.example.Exceptions.ApiException;
import org.example.Parser.Request;
import org.example.Parser.Response;
import org.example.Tokniz.JWTRSA256;
import org.example.User.User;
import org.json.JSONObject;

import java.util.Base64;
@Getter
@Setter
public class ProfileHandler {
    Request request;
     Response response;
    JWTRSA256 keys;


    public ProfileHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();

    }

    public void Handle() throws ApiException {

        if (request.method.equals(Methods.GET)) {
            try {

                JWTRSA256.Authencator(request, Base64.getEncoder().encodeToString(keys.getPublicKey().getEncoded()));
                String userId;
                String token = request.getHeader().getString("token");
                String[] chunks = token.split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();

                String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));
                JSONObject obj = new JSONObject(payload);
                userId = obj.getString("id");

            //!trying fetch the userId
                User user =ProfileServices.GetProfile(userId);
                response.setStauts(Status.OK);
                response.Body.put("name",user.getName());
                response.Body.put("email",user.getEmail());
                response.Body.put("address",user.getAddress());
                response.Body.put("phoneNumber",user.getPhoneNumber());
                response.Body.put("newBalance",user.getCredits());
                response.Body.put("role",user.getRole());
                response.Body.put("cardNumber",user.getVisa().getCardNo());
                response.Body.put("cvv",user.getVisa().getCvv());
                response.Body.put("cardType",user.getVisa().getCardType());
                response.Body.put("mm/yy",user.getVisa().getCardExpiry());
                response.Body.put("cardCarrier",user.getVisa().getCardCarrier());


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
