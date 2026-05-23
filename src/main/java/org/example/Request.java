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
        RequestParser();

    }

    public void RequestParser(){

        try{
        String b = Request.substring(Request.indexOf("{"));
        Body(b);

        }
        catch(StringIndexOutOfBoundsException e)
        {
            Body=null;
        }
        String M_E =Request.substring(0, Request.indexOf("\n"));
        Method(M_E);
        EndPoint(M_E);
        Header();
    }

    private void Body(String b) {

        Body =new JSONObject(b);
    }

    private void Method(String M_E) {
        int index = Request.indexOf(" ");
        String s = Request.substring(0,index);
        method = Methods.valueOf(s) ;

    }
    private void EndPoint(String M_E)
    {
        int Startindex =Request.indexOf("/");
        int lastindex =Request.indexOf(" ",Startindex);
        EndPoint =Request.substring(Startindex,lastindex);

    }
    private void Header()
    {try {
        int Startindex = Request.indexOf("\n");
        int Lastindex = Request.indexOf("{");
        String header = Request.substring(Startindex + 1, Lastindex - 2);
        String[] headers = header.split("\n");
        Header = new JSONObject();
        for (String s : headers) {
            Header.put(s.substring(0, s.indexOf(":")), s.substring(s.indexOf(" ") + 1));
        }
    }
    catch (StringIndexOutOfBoundsException ex)
    {
        Header=null;
    }


    }
}

