/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.engine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.GregorianCalendar;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author agodino
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
                InputStream i = new FileInputStream(file);
                b64 = new String(Base64.encodeBase64(IOUtils.toByteArray(i)));
                i.close();
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

        System.out.println(response.getStatus());

        return response.getStatus() == 200;

    }

//    public static void sendMail(String name, String[] to, String[] bcc, String txt, String subject, AttachMJ evento) throws MailjetException, MailjetSocketTimeoutException {
//
//        MailjetClient client;
//        MailjetRequest request;
//        MailjetResponse response;
//
//        Database dbb = new Database(Action.log);
//        String mailjet_api = dbb.get_Path("mailjet_api");
//        String mailjet_secret = dbb.get_Path("mailjet_secret");
//        String mailjet_name = dbb.get_Path("mailjet_name");
//        dbb.closeDB();
//
//        client = new MailjetClient(mailjet_api, mailjet_secret, new ClientOptions("v3.1"));
//        client.setDebug(MailjetClient.VERBOSE_DEBUG);
//
//        JSONArray dest = new JSONArray();
//        JSONArray ccj = new JSONArray();
//        JSONArray ccn = new JSONArray();
//
//        for (String s : to) {
//            dest.put(new JSONObject().put("Email", s)
//                    .put("Name", ""));
//        }
//
//        if (bcc != null) {
//            for (String s : bcc) {
//                ccn.put(new JSONObject().put("Email", s)
//                        .put("Name", ""));
//            }
//        }
//
//        JSONObject mail = new JSONObject().put(Emailv31.Message.FROM, new JSONObject()
//                .put("Email", mailjet_name)
//                .put("Name", name))
//                .put(Emailv31.Message.TO, dest)
//                .put(Emailv31.Message.CC, ccj)
//                .put(Emailv31.Message.BCC, ccn)
//                .put(Emailv31.Message.SUBJECT, subject)
//                .put(Emailv31.Message.HTMLPART, txt);
//
//        request = new MailjetRequest(Emailv31.resource)
//                .property(Emailv31.MESSAGES, new JSONArray()
//                        .put(mail));
//
//        response = client.post(request);
//
//        System.out.println(response.getStatus());
//
////        return response.getStatus() == 200;
//    }

    private static Calendar conversion(String date) {
        try {
            String data = date.split(" ")[0];
            String anno = data.split("-")[0];
            String mese = data.split("-")[1];
            String giorno = data.split("-")[2];
            String ora = date.split(" ")[1];
            String ore = ora.split(":")[0];
            String min = ora.split(":")[1];
            String sec = ora.split(":")[2];
            Calendar d1 = new GregorianCalendar();
            d1.set(Calendar.MONTH, parseIntR(mese) - 1);
            d1.set(Calendar.DAY_OF_MONTH, parseIntR(giorno));
            d1.set(Calendar.YEAR, parseIntR(anno));
            d1.set(Calendar.HOUR_OF_DAY, parseIntR(ore));
            d1.set(Calendar.MINUTE, parseIntR(min));
            d1.set(Calendar.SECOND, parseIntR(sec));
            return d1;
        } catch (Exception e) {
        }
        return null;
    }

    private static int parseIntR(String value) {
        try {
            if (value != null) {
                return Integer.parseInt(value);
            }
        } catch (Exception e) {
        }
        return 0;
    }

    private static AttachMJ createEVENT(String datainizioMYSQL, String datafineMYSQL, String eventName) {
        try {
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone("Europe/Rome");
            VTimeZone tz = timezone.getVTimeZone();
            Calendar startDate = conversion(datainizioMYSQL);
            startDate.setTimeZone(timezone);
            Calendar endDate = conversion(datafineMYSQL);
            endDate.setTimeZone(timezone);
            DateTime start = new DateTime(startDate.getTime());
            DateTime end = new DateTime(endDate.getTime());
            VEvent meeting = new VEvent(start, end, eventName);
            meeting.getProperties().add(tz.getTimeZoneId());
            UidGenerator ug = new RandomUidGenerator();
            Uid uid = ug.generateUid();
            meeting.getProperties().add(uid);
            net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
            icsCalendar.getProperties().add(new ProdId("-//FL_EventsCalendar//iCal4j 2.0//IT"));
            icsCalendar.getProperties().add(Version.VERSION_2_0);
            icsCalendar.getProperties().add(CalScale.GREGORIAN);
            icsCalendar.getComponents().add(meeting);
            String pathtemp = Action.get_Path("pathTemp");
            new File(pathtemp).mkdirs();
            File ics = new File(pathtemp + uid.getValue() + ".ics");
            FileOutputStream fout = new FileOutputStream(ics);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(icsCalendar, fout);
            fout.close();
            String Filename = "Event_MC.ics";
            String ContentType = "text/calendar";
            InputStream i = new FileInputStream(ics);
            String Base64Content = new String(Base64.encodeBase64(FileUtils.readFileToByteArray(ics)));
            i.close();
            ics.delete();
            return new AttachMJ(ContentType, Filename, Base64Content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
