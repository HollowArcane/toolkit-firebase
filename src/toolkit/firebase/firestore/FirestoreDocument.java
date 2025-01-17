package toolkit.firebase.firestore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import toolkit.firebase.exception.FirebaseException;
import toolkit.firebase.firestore.FirestoreValue.FirestoreTimestamp;
import toolkit.http.HTTPRequestBuilder;
import toolkit.http.HTTPResponse;

public final class FirestoreDocument
{
    private final Map<String, FirestoreValue> fields;
    private final Firestore storage;
    private final String collection;
    private Optional<String> id;
    private Optional<String> name;
    private Optional<Timestamp> createTime;
    private Optional<Timestamp> updateTime;

    FirestoreDocument(Firestore storage, String collection)
    {
        Objects.requireNonNull(storage);
        Objects.requireNonNull(collection);

        this.storage = storage;
        this.collection = collection;
        fields = new HashMap<>();

        id = Optional.empty();
        name = Optional.empty();
        createTime = Optional.empty();
        updateTime = Optional.empty();
    }

    public static FirestoreDocument parse(Firestore storage, String collection, JSONObject object)
    {
        FirestoreDocument document = new FirestoreDocument(storage, collection);
        JSONObject fields = object.getJSONObject("fields");
        for(String key: fields.keySet())
        {
            JSONObject value = fields.getJSONObject(key);
            String type = value.keys().next();
            switch (type)
            {
                case "stringValue" -> document.setString(key, value.getString(type));
                case "integerValue" -> document.setInt(key, value.getInt(type));
                case "doubleValue" -> document.setDouble(key, value.getDouble(type));
                case "booleanValue" -> document.setBoolean(key, value.getBoolean(type));
                case "timestampValue" -> document.setTimestamp(key, FirestoreTimestamp.parse(value.getString(type)));
                case "nullValue" -> document.setNull(key);
            };
        }
        document.setName(object.getString("name"));
        document.setCreateTime(FirestoreTimestamp.parse(object.getString("createTime")));
        document.setUpdateTime(FirestoreTimestamp.parse(object.getString("updateTime")));
        return document;
    }

    private void setName(String name)
    {
        Objects.requireNonNull(name);

        String[] split = name.split("/");
        this.name = Optional.of(name);
        this.id = Optional.of(split[split.length - 1]);
    }
    
    private void setCreateTime(Timestamp timestamp) 
    {
        Objects.requireNonNull(timestamp);
        createTime = Optional.of(timestamp);
    }

    private void setUpdateTime(Timestamp timestamp) 
    {
        Objects.requireNonNull(timestamp);
        updateTime = Optional.of(timestamp);
    }

    public Optional<String> id()
    { return id; }

    public Optional<String> name()
    { return name; }

    public Optional<Timestamp> createTime()
    { return createTime; }

    public Optional<Timestamp> updateTime()
    { return updateTime; }

    public FirestoreValue get(String field)
    { return fields.getOrDefault(field, FirestoreValue.fromNull()); }

    public FirestoreDocument setString(String field, Optional<String> value)
    {
        Objects.requireNonNull(value);
        if(value.isPresent())
        { return this.setString(field, value.get()); }
        return this.setNull(field);
    }

    public FirestoreDocument setString(String field, String value)
    {
        Objects.requireNonNull(value);
        fields.put(field, FirestoreValue.fromString(value));
        return this;
    }

    public FirestoreDocument setInt(String field, int value)
    {
        fields.put(field, FirestoreValue.fromInt(value));
        return this;
    }

    public FirestoreDocument setInt(String field, Optional<Integer> value)
    {
        Objects.requireNonNull(value);
        if(value.isPresent())
        { return this.setInt(field, value.get()); }
        return this.setNull(field);
    }

    public FirestoreDocument setInt(String field, Integer value)
    {
        Objects.requireNonNull(value);
        fields.put(field, FirestoreValue.fromInt(value));
        return this;
    }

    public FirestoreDocument setBoolean(String field, boolean value)
    {
        fields.put(field, FirestoreValue.fromBoolean(value));
        return this;
    }
    
    public FirestoreDocument setBoolean(String field, Optional<Boolean> value)
    {
        Objects.requireNonNull(value);
        if(value.isPresent())
        { return this.setBoolean(field, value.get()); }
        return this.setNull(field);
    }

    public FirestoreDocument setBoolean(String field, Boolean value)
    {
        Objects.requireNonNull(value);
        fields.put(field, FirestoreValue.fromBoolean(value));
        return this;
    }
    
    public FirestoreDocument setDouble(String field, double value)
    {
        fields.put(field, FirestoreValue.fromDouble(value));
        return this;
    }

    public FirestoreDocument setDouble(String field, Optional<Double> value)
    {
        Objects.requireNonNull(value);
        if(value.isPresent())
        { return this.setDouble(field, value.get()); }
        return this.setNull(field);
    }

    public FirestoreDocument setDouble(String field, Double value)
    {
        Objects.requireNonNull(value);
        fields.put(field, FirestoreValue.fromDouble(value));
        return this;
    }
    
    public FirestoreDocument setLocalDateTime(String field, Optional<LocalDateTime> value)
    {
        Objects.requireNonNull(value);
        if(value.isPresent())
        { return this.setLocalDateTime(field, value.get()); }
        return this.setNull(field);
    }

    public FirestoreDocument setLocalDateTime(String field, LocalDateTime value)
    {
        Objects.requireNonNull(value);
        fields.put(field, FirestoreValue.fromLocalDateTime(value));
        return this;
    }

    public FirestoreDocument setTimestamp(String field, Optional<Timestamp> value)
    {
        Objects.requireNonNull(value);
        if(value.isPresent())
        { return this.setTimestamp(field, value.get()); }
        return this.setNull(field);
    }

    public FirestoreDocument setTimestamp(String field, Timestamp value)
    {
        Objects.requireNonNull(value);
        fields.put(field, FirestoreValue.fromTimestamp(value));
        return this;
    }

    public FirestoreDocument setNull(String field)
    {
        fields.put(field, FirestoreValue.fromNull());
        return this;
    }

    public JSONObject json()
    {
        Map<String, Object> jsonFields = new HashMap<>();
        for(String field: fields.keySet())
        {
            jsonFields.put(field, fields.get(field).json());
        }

        return new JSONObject().put("fields", jsonFields);
    }

    public void store()
        throws IOException,
                URISyntaxException,
                FirebaseException
    {
        HTTPResponse response = null;
        if(id.isEmpty())
        {
            String url = "https://firestore.googleapis.com/v1/projects/" + storage.app() + "/databases/(default)/documents/" + collection;
            response = new HTTPRequestBuilder(url)
                .authorization(storage.token())
                .body(json())
                .post();
        }
        else
        {
            String url = "https://firestore.googleapis.com/v1/projects/" + storage.app() + "/databases/(default)/documents/" + collection + "/" + id.get();
            response = new HTTPRequestBuilder(url)
                .authorization(storage.token())
                .body(json())
                .patch();
        }

        try
        {
            setName(response.json().getString("name"));
            setCreateTime(FirestoreTimestamp.parse(response.json().getString("createTime")));
            setUpdateTime(FirestoreTimestamp.parse(response.json().getString("updateTime")));
        }
        catch (JSONException e)
        { throw new FirebaseException(response, e); }
    }
}
