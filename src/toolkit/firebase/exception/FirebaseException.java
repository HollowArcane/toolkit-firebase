package toolkit.firebase.exception;

import java.util.Objects;

import org.json.JSONException;

import toolkit.http.HTTPResponse;

public final class FirebaseException extends Exception
{
    private HTTPResponse response;

    public FirebaseException(HTTPResponse response, JSONException source)
    {
        super(source);
        Objects.requireNonNull(response);
        this.response = response;
    }

    public HTTPResponse getResponse()
    { return response; }
    
    @Override
    public String toString()
    {
        return response.body();
    }
}
