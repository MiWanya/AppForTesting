package com.example.tests;

import com.example.tests.EmailCallback;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;
import android.content.Intent;

public class EmailSender {
    private String username;
    private String password;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String toAddress, String subject, String body, com.example.tests.EmailCallback callback) {
        new SendEmailTask(callback).execute(toAddress, subject, body);
    }

    public void receiveEmail(EmailCallback callback) {
        new ReceiveEmailTask(callback).execute();
    }

    private class SendEmailTask extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private com.example.tests.EmailCallback callback;

        public SendEmailTask(Context context, com.example.tests.EmailCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        public SendEmailTask(com.example.tests.EmailCallback callback) {
            this.callback = callback;
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String toAddress = params[0];
                String subject = params[1];
                String body = params[2];

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.office365.com");
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

                    System.out.println("Письмо успешно отправлено!!!");

                } catch (MessagingException e) {
                    e.printStackTrace();
                    System.out.println("Ошибка при отправке письма: " + e.getMessage());
                }return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;  // Ошибка при отправке
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // Письмо успешно отправлено
                Log.d("EmailSender", "Письмо успешно отправлено!!!");

                // Здесь вы можете запустить новую активити
                if (context instanceof Activity) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    // Если вам нужно, вы можете закрыть текущую активити после перехода
                    ((Activity) context).finish();
                }
            } else {
                // Ошибка при отправке
                Log.d("EmailSender", "Ошибка при отправке письма");
            }

            // Вызываем callback, если он не null
            if (callback != null) {
                callback.onEmailTaskComplete(result);
            }
        }
    }

    private class ReceiveEmailTask extends AsyncTask<Void, Void, Void> {
        private EmailCallback callback;

        public ReceiveEmailTask(EmailCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Properties props = new Properties();
                props.put("mail.imap.ssl.enable", "true");
                props.put("mail.imap.host", "imap.yandex.ru");
                props.put("mail.imap.port", "993");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                Store store = session.getStore("imap");
                store.connect();

                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                Message[] messages = inbox.getMessages();

                for (Message message : messages) {
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println("From: " + message.getFrom()[0]);
                    System.out.println("Body: " + message.getContent());
                }

                inbox.close(false);
                store.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    public interface EmailCallback {
        void onEmailTaskComplete(boolean success);
        void onEmailSent(boolean success);
        void onEmailFailed();
    }
}
