package org.example.SignUp;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.example.Exceptions.ApiException;
import org.example.Parser.Request;
import org.example.Parser.Response;
import org.example.Tokniz.JWTRSA256;
import org.example.User.User;
import org.example.User.UserRepository;
import org.json.JSONObject;
@Getter
@Setter
public class SignUpHandler
{ Request request;
     Response response;
    JWTRSA256 keys;
    User user;
    UserRepository userRepository=UserRepository.getInstance() ;
    public SignUpHandler(Request r, JWTRSA256 keys)
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
            try{user= SignUpService.SignUp(request.Body);
            userRepository.save(user);
                Token =keys.Genrate(user);
                response.setStauts(Status.CREATED);
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
            response.setStauts(Status.METHOD_NOT_ALLOWED);
            response.Body = Body;

        }

    }
}
