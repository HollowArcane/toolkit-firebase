package toolkit.firebase.auth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;

import toolkit.firebase.exception.FirebaseException;
import toolkit.http.HTTPRequestBuilder;
import toolkit.http.HTTPResponse;

public final class Authentication
{
    private final String apiKey;
    public Authentication(String apiKey)
    {
        Objects.requireNonNull(apiKey);
        this.apiKey = apiKey;
    }

    public String register(String email, String password)
        throws IOException,
                URISyntaxException,
                FirebaseException
    {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey;
        HTTPResponse response = new HTTPRequestBuilder(url)
            .body(Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", true
            ))
            .post();

        try
        { return response.json().getString("idToken"); }
        catch (JSONException e)
        { throw new FirebaseException(response, e); }
    }

    public String login(String email, String password)
        throws IOException,
                URISyntaxException,
                FirebaseException
    {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey;
        HTTPResponse response = new HTTPRequestBuilder(url)
            .body(Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", true
            ))
            .post();

        try
        { return response.json().getString("idToken"); }
        catch (JSONException e)
        { throw new FirebaseException(response, e); }
    }

    public String lookup(String idToken)
        throws IOException,
                URISyntaxException,
                FirebaseException

    {
        Objects.requireNonNull(idToken);

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=" + apiKey;
        HTTPResponse response = new HTTPRequestBuilder(url)
            .body(Map.of("idToken", idToken))
            .post();

        try
        { return response.json().getJSONArray("users").getJSONObject(0).getString("email"); }
        catch (JSONException e)
        { throw new FirebaseException(response, e); }
    }
}
