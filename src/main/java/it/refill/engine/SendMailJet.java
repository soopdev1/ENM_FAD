/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.engine;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rcosco
 */
public class SendMailJet {

    public static String convertToUTF8(String s) {
        try {
            return new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
    }

    public static boolean sendMail(String name, String[] to, String[] cc, String txt, String subject) throws MailjetException {
        return sendMail(name, to, cc, new String[]{}, txt, subject, null);
    }

    public static boolean sendMail(String name, String[] to, String[] cc, String[] ccn, String txt, String subject) throws MailjetException {
        return sendMail(name, to, cc, ccn, txt, subject, null);
    }

    public static boolean sendMail(String name, String[] to, String[] cc, String[] bcc, String txt, String subject, File file) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        Database dbb = new Database(Action.log);
        String mailjet_api = dbb.get_Path("mailjet_api");
        String mailjet_secret = dbb.get_Path("mailjet_secret");
        String mailjet_name = dbb.get_Path("mailjet_name");
        dbb.closeDB();

        String filename = "";
        String content_type = "";
        String b64 = "";
        

        ClientOptions options = ClientOptions.builder()
                .apiKey(mailjet_api)
                .apiSecretKey(mailjet_secret)
                .build();

        client = new MailjetClient(options);

        JSONArray dest = new JSONArray();
        JSONArray ccn = new JSONArray();
        JSONArray ccj = new JSONArray();

        if (to != null) {
            for (String s : to) {
                dest.put(new JSONObject().put("Email", s)
                        .put("Name", ""));
            }
        } else {
            dest.put(new JSONObject().put("Email", "")
                    .put("Name", ""));
        }

        if (cc != null) {
            for (String s : cc) {
                ccj.put(new JSONObject().put("Email", s)
                        .put("Name", ""));
            }
        } else {
            ccj.put(new JSONObject().put("Email", "")
                    .put("Name", ""));
        }

        if (bcc != null) {
            for (String s : bcc) {
                ccn.put(new JSONObject().put("Email", s)
                        .put("Name", ""));
            }
        } else {
            ccn.put(new JSONObject().put("Email", "")
                    .put("Name", ""));
        }

        JSONObject mail = new JSONObject().put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", mailjet_name)
                .put("Name", name))
                .put(Emailv31.Message.TO, dest)
                .put(Emailv31.Message.CC, ccj)
                .put(Emailv31.Message.BCC, ccn)
                .put(Emailv31.Message.SUBJECT, subject)
                .put(Emailv31.Message.HTMLPART, txt);

        if (file != null) {
            try {
                filename = file.getName();
                content_type = Files.probeContentType(file.toPath());
                try (InputStream i = new FileInputStream(file)) {
                    b64 = new String(Base64.encodeBase64(IOUtils.toByteArray(i)));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            mail.put(Emailv31.Message.ATTACHMENTS, new JSONArray()
                    .put(new JSONObject()
                            .put("ContentType", content_type)
                            .put("Filename", filename)
                            .put("Base64Content", b64)));
        }

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(mail));

        response = client.post(request);

//        System.out.println(response.getStatus());

        return response.getStatus() == 200;

    }


}
