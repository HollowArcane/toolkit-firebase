package toolkit.firebase.firestore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import toolkit.firebase.exception.FirebaseException;
import toolkit.http.HTTPRequestBuilder;
import toolkit.http.HTTPResponse;

public final class Firestore
{   
    private final String idToken;
    private final String app;

    public Firestore(String idToken, String app)
    {
        Objects.requireNonNull(idToken);
        Objects.requireNonNull(app);
        
        this.idToken = idToken;
        this.app = app;
    }

    String app()
    { return app; }

    String token()
    { return idToken; }

    public List<FirestoreDocument> readAll(String collection)
        throws IOException,
                URISyntaxException,
                FirebaseException
    {
        Objects.requireNonNull(collection);

        String url = "https://firestore.googleapis.com/v1/projects/" + app + "/databases/(default)/documents/" + collection;
        HTTPResponse response = new HTTPRequestBuilder(url)
            .authorization(idToken)
            .get();

        ArrayList<FirestoreDocument> documents = new ArrayList<>();
        try
        {
            JSONArray array = response.json().getJSONArray("documents");
            for(int i = 0; i < array.length(); i++)
            { documents.add(FirestoreDocument.parse(this, collection, array.getJSONObject(i))); }
        }
        catch (JSONException e)
        { throw new FirebaseException(response, e); }
        return documents;
    }

    public Optional<FirestoreDocument> read(String collection, String id)
        throws IOException,
                URISyntaxException,
                FirebaseException
    {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(id);

        String url = "https://firestore.googleapis.com/v1/projects/" + app + "/databases/(default)/documents/" + collection + "/" + id;
        HTTPResponse response = new HTTPRequestBuilder(url)
            .authorization(idToken)
            .get();

        if(response.status() == 404)
        { return Optional.empty(); }

        try
        { return Optional.of(FirestoreDocument.parse(this, collection, response.json())); }
        catch (JSONException e)
        { throw new FirebaseException(response, e); }
    }

    public void delete(String collection, String id)
        throws IOException,
                URISyntaxException,
                FirebaseException
    {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(id);

        String url = "https://firestore.googleapis.com/v1/projects/" + app + "/databases/(default)/documents/" + collection + "/" + id;
        new HTTPRequestBuilder(url)
            .authorization(idToken)
            .delete();
    }

    public FirestoreDocument create(String collection)
    { return new FirestoreDocument(this, collection); }
}
