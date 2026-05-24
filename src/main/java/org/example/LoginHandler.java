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

            try {String userEmail;
                String password;
                try{
                    userEmail =request.getBody().getString("email");

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
                User user = LoginServices.Login(userEmail,password);
                JWTRSA256 genrator = new JWTRSA256();
                String token =genrator.Genrate(user);
                response.Body.put("token",token);
                response.setStauts(Status.OK);


            } catch (ApiException ex) {
                response.setStauts(ex.getStatus());
                JSONObject body = new JSONObject();
                body.put("message", ex.getMessage());
                response.setBody(body);


            }


        } else {
            JSONObject Body = new JSONObject();
            Body.put("message", "Expecting POST on these EndPoint");
            response.stauts = Status.METHOD_NOT_ALLOWED;
            response.Body = Body;

        }

    }
}
