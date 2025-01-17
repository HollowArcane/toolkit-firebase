package toolkit.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public abstract sealed class MessageBuilder permits SimpleMessageBuilder, CompoundMessageBuilder
{
    protected final Message mail;
    MessageBuilder(Session session)
    {
        mail = new MimeMessage(session);   
    }

    public MessageBuilder from(String email)
        throws AddressException,
               MessagingException
    {
        this.mail.setFrom(new InternetAddress(email));
        return this;
    }

    public MessageBuilder to(String email)
        throws AddressException,
               MessagingException
    {
        this.mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        return this;
    }

    public MessageBuilder subject(String subject)
        throws MessagingException
    {
        mail.setSubject(subject);
        return this;
    }

    public void send()
        throws MessagingException
    {
        Transport.send(mail);
    }
}
