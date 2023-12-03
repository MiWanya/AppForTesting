package com.example.tests;
import com.example.tests.EmailCallback;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import android.os.AsyncTask;

public class EmailSender {

    private String username;
    private String password;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String toAddress, String subject, String body,EmailCallback callback) {
        new SendEmailTask(callback).execute(toAddress, subject, body);
    }

    private class SendEmailTask extends AsyncTask<String, Void, Boolean> {
        private EmailCallback callback;

        public SendEmailTask(EmailCallback callback) {
            this.callback = callback;
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try {


                String toAddress = params[0];
                String subject = params[1];
                String body = params[2];

                // Остальной код отправки письма, который ранее был в методе sendEmail
                // ...


                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.mail.ru");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
                    message.setSubject(subject);
                    message.setText(body);

                    Transport.send(message);

                    System.out.println("Письмо успешно отправлено");


                } catch (MessagingException e) {
                    e.printStackTrace();
                    System.out.println("Ошибка при отправке письма: " + e.getMessage());
                }return true;
            }catch (Exception e) {
                e.printStackTrace();
                return false;  // Ошибка при отправке
            }

        }
    }
}
