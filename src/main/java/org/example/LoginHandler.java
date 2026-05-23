package org.example;

import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginHandler {
    Request request;
    Response response;
    JWTRSA256 keys;


    LoginHandler(Request r, JWTRSA256 keys) {
        this.request = r;
        this.keys = keys;
        response = new Response();
        Handle();

    }

    public void Handle() throws ApiException {

        if (request.method.equals(Methods.POST)) {
            //!trying fetch the user
            String userEmail;
            String password;
            try{
                userEmail =request.getBody().getString("userId");

            }

            catch (JSONException E)
            {
                throw new ApiException(Status.BAD_REQUEST, "Missing userEmail ");


            }            try{
                password =request.getBody().getString("password");

            }

            catch (JSONException E)
            {
                throw new ApiException(Status.BAD_REQUEST, "Missing userpassword ");


            }
            try {
                LoginServices.Login(userEmail,password);


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
