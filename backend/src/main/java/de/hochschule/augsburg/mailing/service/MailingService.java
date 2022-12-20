package de.hochschule.augsburg.mailing.service;

import de.hochschule.augsburg.mailing.utility.MailType;
import de.hochschule.augsburg.mailing.utility.Property;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service("mailService")
public class MailingService {

    public void sendEmail(final String studentMail, final MailType mailType, final String content) {
        try {

            System.out.println("TLSEmail Start");
            //create Authenticator object to pass in Session.getInstance argument
            final Session session = Session.getInstance(Property.getMailingProperty(), this.setupAuthenticator());

            final MimeMessage msg = this.setupMessageDetails(session, studentMail, mailType.toString(), content);

            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");


        } catch (final MessagingException e) {
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

    private MimeMessage setupMessageDetails(final Session session, final String studentMail, final String subject,
                                            final String content) throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);
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
