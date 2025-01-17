import javax.mail.*;
import javax.mail.internet.*;

import toolkit.firebase.auth.Authentication;
import toolkit.firebase.exception.FirebaseException;
import toolkit.firebase.firestore.Firestore;
import toolkit.firebase.firestore.FirestoreDocument;
import toolkit.http.HTTPRequestBuilder;
import toolkit.http.HTTPResponse;
import toolkit.mail.Postman;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

public class Main
{
    public static void main(String[] args)
        throws Exception
    {
        // Postman postman = Postman.google(
        //     "tendryrambao@gmail.com",
        //     "foqf rglo xdab doqc"
        // );
        // // Postman postman = new Postman("localhost", 1025).authenticate();

        // postman.createCompoundMessage()
        //             .to("aina.satamandresy@gmail.com")
        //             .subject("Test Email via Gmail SMTP")
        //             .addHTML(Files.readString(Path.of("index.html")))
        //             .attach(new File("image.jpg"), "image-0")
        //             .send();


        // System.out.println("Email sent successfully via Gmail SMTP!");

        
        String token = new Authentication("AIzaSyDp5WZ5uOK530t7ZRKcXwsdpYpga2PQDpo")
            .login("tendryrambao@gmail.com", "12345678");
        
        // new Firestore(token, "prise-en-main---firebase")
        //     .readAll("sports")
        //     .forEach(doc -> {
        //         System.out.println("ID: " + doc.id().get());
        //         System.out.println("Nom: " + doc.get("nom").stringValue());
        //         System.out.println("Annee Creation: " + doc.get("annee_creation").intValue());
        //         System.out.println("Nb Joueurs: " + doc.get("nb_joueur").intValue());
        //         System.out.println();
        //     });

        try
        {
            System.out.println(token);
            FirestoreDocument document = new Firestore(token, "prise-en-main---firebase")
                .read("sports", "m8OVadjjs5cVdeJ4aPwm").get();
    
            document.setString("nom", "mandeh");
            document.setInt("annee_creation", 1900);
            document.setInt("nb_joueur", 13);
            document.store();    
        }
        catch (FirebaseException e)
        {
            System.out.println(e.getResponse().status());
            System.out.println(e.getResponse().body());
            e.printStackTrace();
        }
    }
}
