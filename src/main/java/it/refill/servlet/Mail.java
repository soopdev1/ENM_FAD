/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.servlet;

import com.mailjet.client.errors.MailjetException;
import it.refill.engine.Action;
//import static it.refill.engine.Action.azioneform;
import static it.refill.engine.Action.getRequestValue;
import static it.refill.engine.Action.log;
import it.refill.engine.Database;
import it.refill.engine.GenericUser;
import static it.refill.engine.SendMailJet.sendMail;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import org.apache.commons.validator.routines.EmailValidator;

/**
 *
 * @author rcosco
 */
public class Mail extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        Action.printRequest(request);
//        if (true) {
//            return;
//        }
        try (PrintWriter out = response.getWriter()) {
            String iduser = getRequestValue(request, "iduser");
            String pr = getRequestValue(request, "pr");
//            String tipo = getRequestValue(request, "tipo");
            String datainvito = getRequestValue(request, "datainvito");
            String dataoggi = getRequestValue(request, "dataoggi");
            String st = getRequestValue(request, "st");

            List<GenericUser> usr = Action.get_UserProg(pr);
            if (usr.size() > 0) {
                if (iduser.equals("---")) {
                    usr.forEach(user -> {
                        if (EmailValidator.getInstance().isValid(user.getEmail())) {
                            fadmail(pr, iduser, dataoggi, st, user.getNome().toUpperCase() + " " + user.getCognome().toUpperCase(), datainvito, user.getEmail());
//                            Sms.sendSmsFAD(user.getNome(), user.getCognome(), user.getNumero());
                            out.print("success");
                            out.flush();
                        } else {
                            out.print("ERRORE MAIL NON VALIDA :" + user.getEmail());
                            out.flush();
                        }
                    });
                } else {
                    if (usr.stream().anyMatch(us -> us.getIdallievi().equals(iduser))) {
                        GenericUser user = usr.stream().filter(us -> us.getIdallievi().equals(iduser)).findAny().get();
                        if (user != null) {
                            if (EmailValidator.getInstance().isValid(user.getEmail())) {
                                boolean es = fadmail(pr, iduser, dataoggi, st, user.getNome().toUpperCase() + " " + user.getCognome().toUpperCase(), datainvito, user.getEmail());
//                                Sms.sendSmsFAD(user.getNome(), user.getCognome(), user.getNumero());

                                if(es){
                                    out.print("success");
                                }else{
                                    out.print("ERRORE INVIO MAIL A :" + user.getEmail());
                                }
                                out.flush();
                                out.close();
                            } else {
                                out.print("ERRORE MAIL NON VALIDA :" + user.getEmail());
                                out.flush();
                                out.close();
                            }
                        }
                    }
                }
            }

        }

//        List<GenericUser> usr = Action.get_UserProg(pr);
//        String nomeprogettoform = Action.get_nomeProg(pr);
//        String azioneform = Action.get_Path("linkfad");
//        String datainvito = new DateTime().toString(pat_1);
//
//        if (usr.size() > 0) {
//            if (cf.equals("---")) {
//                usr.forEach(user -> {
//                    if (EmailValidator.getInstance().isValid(user.getEmail())) {
//                        fadmail(nomeprogettoform, datainvito, user.getCognome() + " " + user.getNome(), azioneform, user.getCodicefiscale(), pr, user.getEmail(), st);
//                        Sms.sendSmsFAD(user.getNome(), user.getCognome(), user.getNumero());
//                        System.out.println("MAIL A: " + user.getEmail() + " -- " + user.getCognome() + " " + user.getNome());
//                        out.print("success");
//                        out.flush();
//                        out.close();
//                    } else {
//                        out.print("ERRORE MAIL NON VALIDA :" + user.getEmail());
//                        out.flush();
//                        out.close();
//                    }
//                });
//            } else {
//                if (usr.stream().anyMatch(us -> us.getIdallievi().equals(cf))) {
//                    GenericUser user = usr.stream().filter(us -> us.getIdallievi().equals(cf)).findAny().get();
//                    if (user != null) {
//                        if (EmailValidator.getInstance().isValid(user.getEmail())) {
//                            fadmail(nomeprogettoform, datainvito, user.getCognome() + " " + user.getNome(), azioneform, user.getCodicefiscale(), pr, user.getEmail(), st);
//                            Sms.sendSmsFAD(user.getNome(), user.getCognome(), user.getNumero());
//                            System.out.println("MAIL SINGOLA A: " + user.getEmail() + " -- " + user.getCognome() + " " + user.getNome());
//                            out.print("success");
//                            out.flush();
//                            out.close();
//                        } else {
//                            out.print("ERRORE MAIL NON VALIDA :" + user.getEmail());
//                            out.flush();
//                            out.close();
//                        }
//                    }
//                }
//            }
//        }
    }

//    public static void fadmail_docente(String nomeprogettoform, String datainvito, String nomecognome,
//            String azioneform, String codfiscuser, String proguser, String maildest, String stanza) {
//        String mail = Action.get_Path("fadmail_docente");
//        if (mail != null) {
//            try {
//                new File(pathTEMP).mkdirs();
//                File temp = new File(pathTEMP + Action.generaId(75) + "_temp.html");
//                FileUtils.writeByteArrayToFile(temp, Base64.decodeBase64(mail));
//                String content = Files.asCharSource(temp, StandardCharsets.UTF_8).read();
//                content = StringUtils.replace(content, "@nomeprogettoform", nomeprogettoform);
//                content = StringUtils.replace(content, "@datainvito", datainvito);
//                content = StringUtils.replace(content, "@nomecognome", nomecognome);
//                content = StringUtils.replace(content, "@azioneform", azioneform);
//                content = StringUtils.replace(content, "@codfiscuser", codfiscuser);
//                content = StringUtils.replace(content, "@proguser", proguser + "&roomname=" + stanza);
//                SendMailJet.sendMail("Microcredito", new String[]{maildest}, content, "FAD - Promemoria");
//                temp.delete();
//            } catch (MailjetException | MailjetSocketTimeoutException | IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

    public static boolean fadmail(String idprogetti_formativi, String idsoggetto,
            String dataoggi, String nomestanza, String nomecognome, String datainvito, String emaildest) {

        boolean es = false;
        try {

            String sql4 = "SELECT user,psw FROM fad_access "
                    + "WHERE type='S' "
                    + "AND idprogetti_formativi = " + idprogetti_formativi + " "
                    + "AND idsoggetto = " + idsoggetto + " "
                    + "AND data ='" + dataoggi + "' "
                    + "AND room = '" + nomestanza + "'";
            System.out.println(sql4);
            Database db1 = new Database(log);
            try (Statement st4 = db1.getC().createStatement(); ResultSet rs4 = st4.executeQuery(sql4)) {
                if (rs4.next()) {

                    String user = rs4.getString("user");
                    String psw = RandomStringUtils.randomAlphanumeric(6);
                    String md5psw = DigestUtils.md5Hex(psw);

                    String upd = "UPDATE fad_access SET psw = '" + md5psw + "' WHERE type='S' "
                            + "AND idsoggetto = " + idsoggetto + " "
                            + "AND data ='" + dataoggi + "'";
                    try (Statement st5 = db1.getC().createStatement();) {
                        if (st5.executeUpdate(upd) > 0) {

                            String sql1 = "SELECT ud.fase,lm.giorno,lm.orario_start,lm.orario_end,lm.id_docente FROM lezioni_modelli lm, modelli_progetti mp, lezione_calendario lc, unita_didattiche ud"
                                    + " WHERE mp.id_modello=lm.id_modelli_progetto AND lc.id_lezionecalendario=lm.id_lezionecalendario AND ud.codice=lc.codice_ud"
                                    + " AND mp.id_progettoformativo=" + idprogetti_formativi
                                    + " AND lm.giorno = '" + dataoggi + "'";
                            System.out.println(sql1);
                            try (Statement st1 = db1.getC().createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {
                                if (rs1.next()) {
                                    String orainvito = rs1.getString("lm.orario_start") + " - " + rs1.getString("lm.orario_end");

                                    //INVIO MAIL
                                    String sql6 = "SELECT oggetto,testo FROM email WHERE chiave ='fad3.0'";
                                    try (Statement st6 = db1.getC().createStatement(); ResultSet rs6 = st6.executeQuery(sql6)) {
                                        if (rs6.next()) {
                                            String emailtesto = rs6.getString(2);
                                            String emailoggetto = rs6.getString(1);
                                            
                                            String linkweb = db1.get_Path("linkfad");
                                            String linknohttpweb = remove(linkweb, "https://");
                                            linknohttpweb = remove(linknohttpweb, "http://");
                                            linknohttpweb = removeEnd(linknohttpweb, "/");

                                            emailtesto = StringUtils.replace(emailtesto, "@nomecognome", nomecognome);
                                            emailtesto = StringUtils.replace(emailtesto, "@username", user);
                                            emailtesto = StringUtils.replace(emailtesto, "@password", psw);
                                            emailtesto = StringUtils.replace(emailtesto, "@datainvito", datainvito);
                                            emailtesto = StringUtils.replace(emailtesto, "@orainvito", orainvito);
                                            emailtesto = StringUtils.replace(emailtesto, "@nomestanza", nomestanza);
                                            emailtesto = StringUtils.replace(emailtesto, "@linkweb", linkweb);
                                            emailtesto = StringUtils.replace(emailtesto, "@linknohttpweb", linknohttpweb);
                                            es = sendMail(db1.get_Path("mailsender"), new String[]{emaildest},new String[]{}, emailtesto, emailoggetto);
                                            //sendMail(db1.get_Path("mailsender"), new String[]{"raffaele.cosco@faultless.it"}, emailtesto, emailoggetto);
                                            //es = true;
                                        }
                                    }

                                }
                            }

                        }
                    }

                }
            }
            db1.closeDB();
        } catch (Exception e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        }
        return es;
//        
//        
//        
//        String mail = Action.get_Path("fadmail");
//        if (mail != null) {
//            try {
//                new File(pathTEMP).mkdirs();
//                File temp = new File(pathTEMP + Action.generaId(75) + "_temp.html");
//
//                FileUtils.writeByteArrayToFile(temp, Base64.decodeBase64(mail));
//                String content = Files.asCharSource(temp, StandardCharsets.UTF_8).read();
//                content = StringUtils.replace(content, "@nomeprogettoform", nomeprogettoform);
//                content = StringUtils.replace(content, "@datainvito", datainvito);
//                content = StringUtils.replace(content, "@nomecognome", nomecognome);
//                content = StringUtils.replace(content, "@azioneform", azioneform);
//                content = StringUtils.replace(content, "@codfiscuser", codfiscuser);
//                content = StringUtils.replace(content, "@proguser", proguser + "&roomname=" + stanza);
//                SendMailJet.sendMail("Microcredito", new String[]{maildest}, content, "FAD - Promemoria");
//
//                System.out.println("it.refill.servlet.Mail.fadmail() " + temp.getPath());
//
////                temp.delete();
//                return true;
//            } catch (MailjetException | MailjetSocketTimeoutException | IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return false;
    }

//    public static void fadmail_conference(String idfad, String maildest) {
//        String[] mail = Action.get_Mail("conferenza");
//        if (!mail[0].equals("")) {
//            try {
//                Fadroom fa = Action.getroom(idfad);
//                if (fa != null) {
//                    String content = mail[1];
//                    content = StringUtils.replace(content, "@redirect", get_Path("domino") + "redirect_out.jsp");
//                    content = StringUtils.replace(content, "@link", get_Path("linkfad"));
//                    content = StringUtils.replace(content, "@id", idfad);
//                    content = StringUtils.replace(content, "@user", maildest);
//                    content = StringUtils.replace(content, "@pwd", fa.getPassword());
//                    content = StringUtils.replace(content, "@email_tec", get_Path("emailtecnico"));
//                    content = StringUtils.replace(content, "@email_am", get_Path("emailamministrativo"));
//                    sendMailEvento("Microcredito", new String[]{maildest},
//                            content,
//                            mail[0],
//                            createEVENT(fa.getInizio(), fa.getFine(), mail[0]));
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

//    public static void main(String[] args) {
//        String mail = Action.get_Path("fadmail");
//        if (mail != null) {
//            try {
//                String pathtemp = "/mnt/temp/";
//                new File(pathtemp).mkdirs();
//                File temp = new File(pathtemp + Action.generaId(75) + "_temp.html");
//                FileUtils.writeByteArrayToFile(temp, Base64.decodeBase64(mail));
//                String content = Files.asCharSource(temp, StandardCharsets.UTF_8).read();
//                content = StringUtils.replace(content, "@nomeprogettoform", "");
//                content = StringUtils.replace(content, "@datainvito", "");
//                content = StringUtils.replace(content, "@nomecognome", "");
//                content = StringUtils.replace(content, "@azioneform", azioneform);
//                content = StringUtils.replace(content, "@codfiscuser", "");
//                content = StringUtils.replace(content, "@proguser", "3" + "&roomname=" + "3");
//                SendMailJet.sendMail("Microcredito", new String[]{"rcosco@setacom.it"}, content, "FAD - Promemoria");
//                temp.delete();
////            } catch (IOException ex) {
//            } catch (MailjetException | MailjetSocketTimeoutException | IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
