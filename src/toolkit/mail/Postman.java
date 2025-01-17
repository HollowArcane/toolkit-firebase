package toolkit.mail;

import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;

public class Postman
{
    private Properties properties;
    private String username;
    private Session session;
    
    public Postman(String host, int port)
    {
        Objects.requireNonNull(host);
        
        properties = new Properties();
        set("host", host);
        set("port", port);
    }

    public static Postman google(String email, String password)
    {
        Postman p = new Postman("smtp.gmail.com", 587);
        p.set("ssl.trust", "smtp.gmail.com"); 

        p.authenticate(email, password);
        return p;
    }

    public Postman authenticate()
    {
        session = Session.getInstance(properties);
        return this;
    }

    public Postman authenticate(String email, String password)
    {
        set("auth", "true");
        set("starttls.enable", "true");

        username = email;
        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        return this;
    }

    public Postman setRaw(String property, Object value)
    {
        Objects.requireNonNull(property);
        Objects.requireNonNull(value);
        properties.put(property, value);
        return this;
    }

    public Postman set(String property, Object value)
    {
        Objects.requireNonNull(property);
        Objects.requireNonNull(value);
        properties.put("mail.smtp." + property, value);
        return this;
    }

    public SimpleMessageBuilder createSimpleMessage()
        throws AddressException,
               MessagingException
    {
        SimpleMessageBuilder builder = new SimpleMessageBuilder(session);
        if(username != null)
        { builder.from(username); }
        return builder;
    }

    public CompoundMessageBuilder createCompoundMessage()
        throws AddressException,
               MessagingException
    {
        CompoundMessageBuilder builder = new CompoundMessageBuilder(session);
        if(username != null)
        { builder.from(username); }
        return builder;
    }
}
