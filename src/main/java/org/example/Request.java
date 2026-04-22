package org.example;
import org.example.Enums.Methods;
import org.json.JSONObject;

public class Request {
    String Request;
    JSONObject Body;
    Methods method;

    Request(String s) {
        Request = s;

    }

    public JSONObject GetBody() {
        int index = Request.indexOf("[CRLF]");
        String body = Request.substring(index + 6);
        Body = new JSONObject(body);
        return Body;
    }

    public  Methods GetMethod() {
        int index = Request.indexOf("/");
        String s = Request.substring(1,index);
        method = Methods.valueOf(s) ;
        return method;
    }
}

