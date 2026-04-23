package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.example.Enums.Status;
import org.json.JSONObject;
@Getter
@Setter
public class Response {
    Status stauts;
    String message;
    JSONObject Header;
    JSONObject Body;

    public String ToString()
    {

        StringBuilder response=new StringBuilder();
        response.append(stauts.getCode());
        response.append(" ");
        response.append(stauts.getMessage()+"\n");
        if(Header!=null)
        response.append(Header.toString(4)+"\n");
        response.append("[CRLF]\n");
        if(Body!=null)
            response.append(Body.toString(4));
        return response.toString();
    }

}
