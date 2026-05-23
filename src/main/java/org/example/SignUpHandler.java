package org.example;

import com.auth0.jwt.interfaces.Header;
import com.google.gson.Gson;
import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUpHandler
{ Request request;
    Response response;
    JWTRSA256 keys;
    User user;
    SignUpHandler(Request r,JWTRSA256 keys)
    {
        this.request=r;
        this.keys=keys;
        response=new Response();
        Handle();

    }
    public void Handle ()throws ApiException {
            String Token ;
        if (request.method.equals(Methods.POST)) {

            //!creating the token
            //! taking the request body and convert to Map to throw to the method
//            HashMap<String,String> payload =new Gson().fromJson(request.Body.toString(),HashMap.class);
            //!pass the user to the data base
            try{user=SignUpService.SignUp(request.Body);
                Token =keys.Genrate(user);
                response.stauts = Status.ACCEPTED;
                response.Body = new JSONObject();
                response.Body.put("token", Token);}
            catch (ApiException ex)
            {
                response.setStauts(ex.getStatus());
                JSONObject body = new JSONObject();
                body.put("message",ex.getMessage());
                response.setBody(body);


            }





        }
        else {
             JSONObject Body = new JSONObject();
            Body.put("message", "Expecting GET on these EndPoint");
            response.stauts = Status.METHOD_NOT_ALLOWED;
            response.Body = Body;

        }

    }
}
