package org.example.SearchUser;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.example.Enums.Roles;
import org.example.Enums.Status;
import org.example.Exceptions.ApiException;
import org.example.Parser.Request;
import org.example.Parser.Response;
import org.example.Profile.ProfileServices;
import org.example.Tokniz.JWTRSA256;
import org.example.User.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.management.relation.Role;
import java.util.Base64;
@Getter
@Setter
public class SearchUserHandler {

    Request request;
    Response response;
    JWTRSA256 keys;


    public SearchUserHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();

    }

    public void Handle() throws ApiException {

        if (request.method.equals(Methods.GET)) {
            try {
                JWTRSA256.Authencator(request, Base64.getEncoder().encodeToString(keys.getPublicKey().getEncoded()));
                Roles role;
                String token = request.getHeader().getString("token");
                String[] chunks = token.split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();

                String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));
                JSONObject obj = new JSONObject(payload);
                role = Roles.valueOf(obj.getString("role")) ;
                String name;
              try{   name =request.getBody().getString("name");}
              catch(JSONException e)
              {
                  throw new ApiException(Status.BAD_REQUEST,"Missing Search name");
              }
                JSONArray b = SearchUserServices.Search(role,name);
              response.setStauts(Status.OK);

              response.getBody().put("users",b);

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


