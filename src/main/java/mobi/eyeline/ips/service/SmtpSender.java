package mobi.eyeline.ips.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

class SmtpSender {

  private final String smtpHost;
  private final int smtpPort;
  private final String smtpUsername;
  private final String smtpPassword;

  private final String mailFrom;

  public SmtpSender(String smtpHost,
                    int smtpPort,
                    String smtpUsername,
                    String smtpPassword,
                    String mailFrom) {

    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    this.smtpUsername = smtpUsername;
    this.smtpPassword = smtpPassword;

    this.mailFrom = mailFrom;
  }

  public void send(MailService.Message message)
      throws EmailException {

    final HtmlEmail email = new HtmlEmail();

    email.setHostName(smtpHost);
    email.setSmtpPort(smtpPort);
    email.setAuthentication(smtpUsername, smtpPassword);
    email.setCharset("UTF-8");

    email
        .setHtmlMsg(message.getBody())
        .setFrom(mailFrom, "IPS")
        .addTo(message.getTargetEmail())
        .setSubject(message.getSubject())
        .send();
  }
}
