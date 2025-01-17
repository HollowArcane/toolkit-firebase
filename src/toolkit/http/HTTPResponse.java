package toolkit.http;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPResponse
{
    private int status;
    private Scanner scanner;
    private String body;

    public HTTPResponse(HTTPRequestBuilder builder, int status)
        throws IOException
    {
        Objects.requireNonNull(builder);
        this.status = status;

        if(ok())
        { this.scanner = new Scanner(builder
                .getConnection()
                .getInputStream()); }
        else
        { this.scanner = new Scanner(builder
                .getConnection()
                .getErrorStream()); }
    }

    public boolean ok()
    { return status / 100 == 2; }

    public int status()
    { return status; }

    public String body()
    {
        if(body == null)
        {
            StringBuilder builder = new StringBuilder();
            while(scanner.hasNext())
            { builder.append(scanner.nextLine()); }
    
            scanner.close();
            this.body = builder.toString();
        }
        return body;
    }

    public JSONObject json()
        throws JSONException
    { return new JSONObject(body()); }
}
