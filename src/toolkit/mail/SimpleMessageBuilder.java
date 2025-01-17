package toolkit.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;

public final class SimpleMessageBuilder extends MessageBuilder
{
    SimpleMessageBuilder(Session session)
    { super(session); }

    
    @Override
    public SimpleMessageBuilder from(String email)
        throws AddressException, MessagingException
    { return (SimpleMessageBuilder)super.from(email); }

    @Override
    public SimpleMessageBuilder to(String email)
        throws AddressException, MessagingException
    { return (SimpleMessageBuilder)super.to(email); }

    @Override
    public SimpleMessageBuilder subject(String email)
        throws AddressException, MessagingException
    { return (SimpleMessageBuilder)super.subject(email); }


    public MessageBuilder body(String content)
        throws MessagingException
    {
        mail.setText(content);
        return this;
    }

    public MessageBuilder bodyHTML(String content)
        throws MessagingException
    {
        mail.setContent(content, "text/html");
        return this;
    }    
}
