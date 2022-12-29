package de.hochschule.augsburg.mailing.service;

import de.hochschule.augsburg.mailing.utility.MailType;
import de.hochschule.augsburg.mailing.utility.Property;
import de.hochschule.augsburg.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@RequiredArgsConstructor
@Service("mailService")
public class MailingService {

    private SecurityService securityService;

    public void sendEmail(MailType mailType, String content) {
        try {

            String studentMail = "shkelzen.veliqi1@hs-augsburg.de";
            System.out.println("TLSEmail Start");
            //create Authenticator object to pass in Session.getInstance argument
            Session session = Session.getInstance(Property.getMailingProperty(), setupAuthenticator());

            MimeMessage msg = setupMessageDetails(session, studentMail, mailType.toString(), content);

            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Authenticator setupAuthenticator() {
        return new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        Property.getApplicationProperty().getProperty("spring.mail.username"),
                        Property.getApplicationProperty().getProperty("mailing.app.password"));
            }
        };
    }

    private MimeMessage setupMessageDetails(Session session, String studentMail, String subject,
                                            String content) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(
                new InternetAddress(Property.getApplicationProperty().getProperty("spring.mail.username", "NoReply")));
        msg.setReplyTo(InternetAddress.parse(studentMail, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(content, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(studentMail, false));
        System.out.println("Message is ready");
        return msg;
    }

}
