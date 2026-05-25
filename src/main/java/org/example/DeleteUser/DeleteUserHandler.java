package org.example.DeleteUser;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.example.Enums.Roles;
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
public class DeleteUserHandler {
    Request request;
    Response response;
    JWTRSA256 keys;


    public DeleteUserHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();

    }


    public void Handle() throws ApiException {
        if (request.method.equals(Methods.DELETE)) {
            //! checking the Token
            try {

                JWTRSA256.Authencator(request, Base64.getEncoder()
                        .encodeToString(keys.getPublicKey().getEncoded()));
                String token;
                String sellerId;
                String userId;
                Roles role;
                token = request.getHeader().getString("token");
                String[] chunks = token.split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();

                String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));
                JSONObject obj = new JSONObject(payload);
                userId = obj.getString("id");
                role = Roles.valueOf(obj.getString("role"));
                if (!role.equals(Roles.Admin)) {
                    throw new ApiException(Status.UNAUTHORIZED, "You are not admin operation terminated");
                }
                String Id;
                try {
                    Id = request.getBody().getString("id");
                } catch (JSONException eex) {
                    throw new ApiException(Status.BAD_REQUEST, "User Not Found");

                }
                DeleteUserServices.delete(Id);
                response.setStauts(Status.ACCEPTED);
            } catch (ApiException ex) {
                response.setStauts(ex.getStatus());
                JSONObject body = new JSONObject();
                body.put("message", ex.getMessage());
                response.setBody(body);
            }
        }
        else {
            JSONObject Body = new JSONObject();
            Body.put("message", "Expecting DELETE on these EndPoint");
            response.setStauts(Status.METHOD_NOT_ALLOWED);
            response.Body = Body;

        }
    }
}