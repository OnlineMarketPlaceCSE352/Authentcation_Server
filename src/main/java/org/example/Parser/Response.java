package org.example.Parser;

import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Status;
import org.json.JSONObject;

@Getter
@Setter
public class Response {
    Status stauts;
    JSONObject Header;
    public JSONObject Body;
    public Response()
    {
        Header =new JSONObject();
        Body =new JSONObject();
    }

    public String ToString()
    {

        StringBuilder response=new StringBuilder();
        response.append("HTTP/1.1 ");
        response.append(stauts.getCode()+" ");
        response.append(stauts.getMessage()+"\r\n");
        response.append("Content-Type: application/json");

        response.append("\r\n");
        if(!Header.isEmpty())
        {
            for(String key : Header.keySet())
            {
                Object value = Header.get(key);
                response.append(key+": "+value);
            }
        response.append("\r\n");}
        response.append("\r\n");
        if(Body!=null)
            response.append(Body.toString(4));
        response.append("\r\n");
        return response.toString();
    }

}
