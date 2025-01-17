package toolkit.firebase.firestore;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONObject;

public final class FirestoreValue
{
    private static final FirestoreValue FIRESTORE_NULL_VALUE = new FirestoreValue("nullValue", new JSONObject());
    private String type;
    private Object value;

    public static final class Null { private Null() {} }

    public static class FirestoreTimestamp
    {
        private Timestamp timestamp;
        public FirestoreTimestamp(Timestamp timestamp)
        {
            this.timestamp = timestamp;
        }

        public static Timestamp parse(String value)
        { return Timestamp.valueOf(value.replace('T', ' ').replace("Z", "")); }

        @Override
        public String toString()
        { return timestamp.toString().replace(' ', 'T') + "Z"; }
    }

    private FirestoreValue(String type, Object value)
    {
        this.type = type;
        this.value = value;
    }

    public String type()
    { return type; }

    public Integer intValue()
    { return (Integer)value; }

    public Double doubleValue()
    { return (Double)value; }

    public String stringValue()
    { return String.valueOf(value); }

    public Boolean booleanValue()
    { return (Boolean)value; }

    public Null nullValue()
    {
        if(value == null)
        { throw new UnsupportedOperationException(); }
        return null;
    }

    public Timestamp timestampValue()
    { return ((FirestoreTimestamp)value).timestamp; }

    public LocalDateTime localDateTimeValue()
    { return ((FirestoreTimestamp)value).timestamp.toLocalDateTime(); }

    public Optional<Integer> intMaybe()
    {
        if(value == null)
        { return Optional.empty(); }
        return Optional.ofNullable((Integer)value);
    }

    public Optional<Double> doubleMaybe()
    {
        if(value == null)
        { return Optional.empty(); }
        return Optional.ofNullable((Double)value);
    }

    public Optional<String> stringMaybe()
    {
        if(value == null)
        { return Optional.empty(); }
        return Optional.ofNullable((String)value);
    }

    public Optional<Boolean> booleanMaybe()
    {
        if(value == null)
        { return Optional.empty(); }
        return Optional.ofNullable((Boolean)value);
    }

    public Optional<Timestamp> timestampMaybe()
    {
        if(value == null)
        { return Optional.empty(); }
        return Optional.ofNullable(((FirestoreTimestamp)value).timestamp);
    }

    public Optional<LocalDateTime> localDateTimeMaybe()
    {
        if(value == null)
        { return Optional.empty(); }
        return Optional.ofNullable(((FirestoreTimestamp)value).timestamp.toLocalDateTime());
    }

    public static FirestoreValue fromString(String value)
    {
        Objects.requireNonNull(value);
        return new FirestoreValue("stringValue", value);
    }

    public static FirestoreValue fromInt(int value)
    { return new FirestoreValue("integerValue", value); }

    public static FirestoreValue fromInt(Integer value)
    {
        Objects.requireNonNull(value);
        return new FirestoreValue("integerValue", value);
    }

    public static FirestoreValue fromDouble(double value)
    { return new FirestoreValue("doubleValue", value); }

    public static FirestoreValue fromDouble(Double value)
    {
        Objects.requireNonNull(value);
        return new FirestoreValue("doubleValue", value);
    }

    public static FirestoreValue fromBoolean(boolean value)
    { return new FirestoreValue("booleanValue", value); }

    public static FirestoreValue fromBoolean(Boolean value)
    {
        Objects.requireNonNull(value);
        return new FirestoreValue("booleanValue", value);
    }

    public static FirestoreValue fromNull()
    { return FIRESTORE_NULL_VALUE; }

    public static FirestoreValue fromTimestamp(Timestamp timestamp)
    {
        Objects.requireNonNull(timestamp);
        return new FirestoreValue("timestampValue", new FirestoreTimestamp(timestamp));
    }

    public static FirestoreValue fromLocalDateTime(LocalDateTime datetime)
    { return fromTimestamp(Timestamp.valueOf(datetime)); }

    public JSONObject json()
    { return new JSONObject().put(type, String.valueOf(value)); }
}
