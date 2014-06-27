package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Email notifications support.
 */
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final TemplateService templateService;
    private final BlockingQueue<Message> pool;

    public MailService(TemplateService templateService, SmtpSender smtpSender) {
        this.templateService = templateService;

        pool = new LinkedBlockingQueue<>();

        final MailThread sendingThread = new MailThread(pool, smtpSender);
        sendingThread.start();

        logger.debug("Mail service initialized");
    }

    public void sendUserRegistration(User user, String rawPassword) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.user.registration", user.getLocale().asLocale()),
                templateService.formatUserRegistration(user, rawPassword)
        ));
    }

    public void sendUserModified(User user) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.user.modified", user.getLocale().asLocale()),
                templateService.formatUserModified(user)
        ));
    }

    public void sendUserModified(User user, String oldEmail) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.user.modified", user.getLocale().asLocale()),
                templateService.formatUserModified(user)
        ));

        send(new Message(
                oldEmail,
                getSubject("email.subject.user.modified", user.getLocale().asLocale()),
                templateService.formatUserModified(user)
        ));
    }

    public void sendUserDeactivation(User user) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.user.deactivation", user.getLocale().asLocale()),
                templateService.formatUserDeactivation(user)
        ));
    }

    public void sendUserActivation(User user) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.user.activation", user.getLocale().asLocale()),
                templateService.formatUserActivation(user)
        ));
    }

    public void sendPasswordRestore(User user, String rawNewPassword) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.password.restore", user.getLocale().asLocale()),
                templateService.formatPasswordRestore(user, rawNewPassword)
        ));
    }

    public void sendCouponRemaining(User user,
                                    Survey survey,
                                    int percent,
                                    long remainingNumber) {
        final String body = templateService.formatCouponRemaining(
                user,
                survey,
                percent,
                remainingNumber);

        send(new Message(
                user.getEmail(),
                getSubject("email.subject.coupon.notification", user.getLocale().asLocale()),
                body
        ));
    }

    public void sendCouponExhaustion(User user, Survey survey) {
        send(new Message(
                user.getEmail(),
                getSubject("email.subject.coupon.notification", user.getLocale().asLocale()),
                templateService.formatCouponExhaustion(user, survey)
        ));
    }

    private String getSubject(String key, Locale locale) {
        final ResourceBundle bundle = ResourceBundle.getBundle("email", locale);
        return bundle.getString(key);
    }

    private void send(Message message) {
        try {
            pool.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //
    //
    //

    static class Message {

        private String targetEmail;
        private String subject;
        private String body;

        private Message(String targetEmail, String subject, String body) {
            this.targetEmail = targetEmail;
            this.subject = subject;
            this.body = body;
        }

        public String getTargetEmail() {
            return targetEmail;
        }

        public void setTargetEmail(String targetEmail) {
            this.targetEmail = targetEmail;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "targetEmail='" + targetEmail + '\'' +
                    '}';
        }
    }


    //
    //
    //

    private static class MailThread extends Thread {

        private static final Logger logger = LoggerFactory.getLogger(MailThread.class);

        private final BlockingQueue<Message> pool;
        private final SmtpSender sender;


        public MailThread(BlockingQueue<Message> pool, SmtpSender sender) {
            super("mail");

            this.pool = pool;
            this.sender = sender;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    trySend(pool.take());
                }
            } catch (InterruptedException ignored) {}
        }

        private void trySend(Message message) {
            logger.debug("Sending message: " + message);
            try {
                sender.send(message);

            } catch (EmailException e) {
                logger.error("Message sending failed: " + message, e);
            }
        }
    }
}