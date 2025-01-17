package toolkit.mail;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public final class CompoundMessageBuilder extends MessageBuilder
{
    private final Multipart parts;

    CompoundMessageBuilder(Session session)
    {
        super(session);
        parts = new MimeMultipart("related");
    }

    @Override
    public CompoundMessageBuilder from(String email)
        throws AddressException, MessagingException
    { return (CompoundMessageBuilder)super.from(email); }

    @Override
    public CompoundMessageBuilder to(String email)
        throws AddressException, MessagingException
    { return (CompoundMessageBuilder)super.to(email); }

    @Override
    public CompoundMessageBuilder subject(String email)
        throws AddressException, MessagingException
    { return (CompoundMessageBuilder)super.subject(email); }

    public CompoundMessageBuilder attach(File file, String id)
        throws MessagingException
    {
        MimeBodyPart part = new MimeBodyPart();
        DataSource fds = new FileDataSource(file);

        part.setDataHandler(new DataHandler(fds));
        part.setHeader("Content-ID", "<" + id + ">");
        part.setFileName(file.getName());
        
        parts.addBodyPart(part);
        return this;
    }

    public CompoundMessageBuilder addHTML(String html)
        throws MessagingException
    {
        MimeBodyPart part = new MimeBodyPart();
        part.setContent(html, "text/html");

        parts.addBodyPart(part);
        return this;
    }

    public CompoundMessageBuilder addText(String text)
        throws MessagingException
    {
        MimeBodyPart part = new MimeBodyPart();
        part.setText(text);

        parts.addBodyPart(part);
        return this;
    }

    @Override
    public void send()
        throws MessagingException
    {
        mail.setContent(parts);
        super.send();
    }
}
