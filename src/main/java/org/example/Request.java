package org.example;
import lombok.Getter;
import lombok.Setter;
import org.example.Enums.Methods;
import org.json.JSONObject;
@Getter
@Setter
public class Request {
   public String Request;
   public JSONObject Body;
   public Methods method;
   public String EndPoint;
   public JSONObject Header;

    Request(String s) {
        Request = s;
        Body();
        Method();
        EndPoint();
        Header();

    }
    public void RequestHandler(){
        Body();
        Method();
        EndPoint();
        Header();
    }

    private void Body() {
        int index = Request.indexOf("[CRLF]");
        String body = Request.substring(index + 6);
        Body = new JSONObject(body);

    }

    private void Method() {
        int index = Request.indexOf("/");
        String s = Request.substring(0,index-1);
        method = Methods.valueOf(s) ;

    }
    private void EndPoint()
    {
        int Startindex =Request.indexOf("/api");
        int lastindex =Request.indexOf("{");
        EndPoint =Request.substring(Startindex,lastindex-1);

    }
    private void Header()
    {
        int Startindex= Request.indexOf("{");
        int Lastindex= Request.indexOf("}");
        String header =Request.substring(Startindex,Lastindex+1);
        Header =new JSONObject(header);


    }
}

