package com.example.eventec.email;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import com.google.zxing.WriterException;


public class SendMail {
    private String toEmail;
    private String subject;
    private String message;
    private String QRData;

    public SendMail(String toEmail, String subject, String message) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.message = message;
    }

    public void setQRData(String QRData) {
        this.QRData = QRData;
    }


    public void execute(boolean isQR) {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                final String fromEmail = "ncqueescribir@gmail.com"; // requires valid gmail id
                final String password = "dggc wajc szuf auxq"; // correct password for gmail id

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
                props.put("mail.smtp.port", "587"); // TLS Port
                props.put("mail.smtp.auth", "true"); // enable authentication
                props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

                Authenticator auth = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                };

                Session session = Session.getInstance(props, auth);

                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(fromEmail));
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                    msg.setSubject(subject);

                    // create the message part
                    MimeBodyPart content = new MimeBodyPart();
                    content.setText(message);

                    // Si es un QR, se agrega el QR al correo
                    if (isQR) {
                        // generate QR code
                        Bitmap bitmap = QRCodeGenerator.generateQRCodeImage(QRData, 350, 350);
                        // create the image part
                        MimeBodyPart imagePart = new MimeBodyPart();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        ByteArrayDataSource bds = new ByteArrayDataSource(bitmapdata, "image/png");
                        imagePart.setDataHandler(new DataHandler(bds));
                        imagePart.setHeader("Content-ID", "<image>");

                        // create the multipart
                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(content);
                        multipart.addBodyPart(imagePart);

                        // set the multipart as the message's content
                        msg.setContent(multipart);
                    } else {
                        msg.setContent(message, "text/html; charset=utf-8");
                    }

                    Transport.send(msg);
                } catch (MessagingException | WriterException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }.execute();
    }
}