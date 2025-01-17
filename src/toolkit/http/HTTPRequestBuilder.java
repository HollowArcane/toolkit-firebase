package toolkit.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

public class HTTPRequestBuilder
{
    public static enum Method
    {
        POST, GET, PUT, PATCH, DELETE, OPTIONS
    }

    private URL url;
    private HttpURLConnection connection;
    private Object body;

    public HTTPRequestBuilder(String url)
        throws URISyntaxException,
                IOException
    {
        Objects.requireNonNull(url);
        this.url = new URI(url).toURL();

        connection = (HttpURLConnection)this.url.openConnection();
        connection.setRequestMethod(Method.GET.name());
        connection.setDoOutput(true);
    }
    
    HttpURLConnection getConnection()
    { return connection; }

    public HTTPRequestBuilder header(String header, String value)
    {
        Objects.requireNonNull(header);
        Objects.requireNonNull(value);
        connection.setRequestProperty(header, value);
        return this;
    }

    public HTTPRequestBuilder contentType(String contentType)
    { return header("Content-Type", contentType); }

    public HTTPRequestBuilder authorization(String authorization)
    { return header("Authorization", "Bearer " + authorization); }


    public HTTPRequestBuilder body(String raw)
    {
        this.body = raw;
        return this;
    }

    public HTTPRequestBuilder body(JSONObject json)
    {
        this.body = json;
        return contentType("application/json");
    }

    public HTTPRequestBuilder body(Map<String, ?> map)
    {
        this.body = new JSONObject(map);
        return contentType("application/json");
    }

    public HTTPResponse get()
        throws IOException
    {
        connection.setRequestMethod(Method.GET.name());
        return send();
    }

    public HTTPResponse post()
        throws IOException
    {
        connection.setRequestMethod(Method.POST.name());
        return send();
    }

    public HTTPResponse put()
        throws IOException
    {
        connection.setRequestMethod(Method.PUT.name());
        return send();
    }

    public HTTPResponse patch()
        throws IOException
    {
        connection.setRequestMethod(Method.POST.name());
        connection.setRequestProperty("X-HTTP-Method-Override", Method.PATCH.name());
        return send();
    }

    public HTTPResponse delete()
        throws IOException
    {
        connection.setRequestMethod(Method.DELETE.name());
        return send();
    }

    public HTTPResponse options()
        throws IOException
    {
        connection.setRequestMethod(Method.OPTIONS.name());
        return send();
    }

    private HTTPResponse send()
        throws IOException
    {
        
        if(body != null)
        {
            try(OutputStream os = connection.getOutputStream())
            {
                os.write(body.toString().getBytes());
                os.flush();
            }
        }
        return new HTTPResponse(this, connection.getResponseCode());
    }
}
