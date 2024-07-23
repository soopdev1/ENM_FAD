package rc.so.servlet;

import rc.so.engine.Action;
import static rc.so.engine.Action.estraiEccezione;
import static rc.so.engine.Action.getRequestValue;
import static rc.so.engine.Action.log;
import rc.so.engine.Database;
import rc.so.sso.DbSSO;
import rc.so.engine.GenericUser;
import static rc.so.engine.SendMailJet.sendMail;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
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
public class Mail_Docenti extends HttpServlet {

    public static boolean fadmail_DOCENTE(String idprogetti_formativi, String id_docente,
            String dataoggi, String nomestanza, String nomecognome, String datainvito, String emaildest) {

        boolean es = false;
        try {

            String sql4 = "SELECT user,psw FROM fad_access WHERE type='D' AND idprogetti_formativi = ? AND idsoggetto = ? AND data = ? AND room = ?";
            DbSSO dbs = new DbSSO();
            Database db1 = new Database(log);
            String linkweb = db1.get_Path("linkfad");
            String linknohttpweb = remove(linkweb, "https://");
            linknohttpweb = remove(linknohttpweb, "http://");
            linknohttpweb = removeEnd(linknohttpweb, "/");
            String sender = db1.get_Path("mailsender");

            try (PreparedStatement ps4 = db1.getC().prepareStatement(sql4)) {
                ps4.setString(1, idprogetti_formativi);
                ps4.setString(2, id_docente);
                ps4.setString(3, dataoggi);
                ps4.setString(4, nomestanza);
                try (ResultSet rs4 = ps4.executeQuery()) {
                    if (rs4.next()) {

                        String user = rs4.getString("user");
                        String psw = RandomStringUtils.randomAlphanumeric(6);
                        String md5psw = DigestUtils.md5Hex(psw);

                        String upd = "UPDATE fad_access SET psw = ? WHERE type='D' AND idsoggetto = ? AND data = ?";
                        try (PreparedStatement ps5 = db1.getC().prepareStatement(upd)) {
                            ps5.setString(1, md5psw);
                            ps5.setString(2, id_docente);
                            ps5.setString(3, dataoggi);
                            if (ps5.executeUpdate() > 0) {
                                log.log(Level.INFO, "SSO ALLIEVO ) {0} : {1}",
                                        new Object[] { nomecognome, dbs.executequery(upd) });
                                String sql1 = "SELECT ud.fase,lm.giorno,lm.orario_start,lm.orario_end,lm.id_docente "
                                        + "FROM lezioni_modelli lm, modelli_progetti mp, lezione_calendario lc, unita_didattiche ud, fad_multi f"
                                        + " WHERE mp.id_modello=lm.id_modelli_progetto AND lc.id_lezionecalendario=lm.id_lezionecalendario AND ud.codice=lc.codice_ud"
                                        + " AND f.idprogetti_formativi=mp.id_progettoformativo AND mp.id_progettoformativo =? AND f.nomestanza = ? AND lm.giorno = ?";
                                try (PreparedStatement ps1 = db1.getC().prepareStatement(sql1)) {
                                    ps1.setString(1, idprogetti_formativi);
                                    ps1.setString(2, nomestanza);
                                    ps1.setString(3, dataoggi);
                                    try (ResultSet rs1 = ps1.executeQuery()) {
                                        if (rs1.next()) {
                                            String orainvito = rs1.getString("lm.orario_start") + " - "
                                                    + rs1.getString("lm.orario_end");

                                            // INVIO MAIL
                                            String sql6 = "SELECT oggetto,testo FROM email WHERE chiave ='fad3.0_DOCENTE'";
                                            try (Statement st6 = db1.getC().createStatement();
                                                    ResultSet rs6 = st6.executeQuery(sql6)) {
                                                if (rs6.next()) {
                                                    String emailtesto = rs6.getString(2);
                                                    String emailoggetto = rs6.getString(1);
                                                    emailtesto = StringUtils.replace(emailtesto, "@nomecognome",
                                                            nomecognome);
                                                    emailtesto = StringUtils.replace(emailtesto, "@username", user);
                                                    emailtesto = StringUtils.replace(emailtesto, "@password", psw);
                                                    emailtesto = StringUtils.replace(emailtesto, "@datainvito",
                                                            datainvito);
                                                    emailtesto = StringUtils.replace(emailtesto, "@orainvito",
                                                            orainvito);
                                                    emailtesto = StringUtils.replace(emailtesto, "@nomestanza",
                                                            nomestanza);
                                                    emailtesto = StringUtils.replace(emailtesto, "@linkweb", linkweb);
                                                    emailtesto = StringUtils.replace(emailtesto, "@linknohttpweb",
                                                            linknohttpweb);
                                                    es = sendMail(sender, new String[] { emaildest }, new String[] {},
                                                            emailtesto, emailoggetto);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            db1.closeDB();
            dbs.closeDB();
        } catch (Exception e) {
            log.severe(estraiEccezione(e));
        }
        return es;

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) {
            String id_docente = getRequestValue(request, "iduser");
            String pr = getRequestValue(request, "pr");
            // String tipo = getRequestValue(request, "tipo");
            String datainvito = getRequestValue(request, "datainvito");
            String dataoggi = getRequestValue(request, "dataoggi");
            String st = getRequestValue(request, "st");

            List<GenericUser> lista_docenti = Action.get_DocProg(pr);

            if (lista_docenti.stream().anyMatch(us -> us.getIdallievi().equals(id_docente))) {
                Optional<GenericUser> optionalDocente = lista_docenti.stream()
                        .filter(us -> us.getIdallievi().equals(id_docente)).findAny();
                if (optionalDocente.isPresent()) {
                    GenericUser docente = optionalDocente.get();
                    String maildest = docente.getEmail();
                    if (!EmailValidator.getInstance().isValid(maildest)) {
                        out.print("MAIL NON VALIDA :" + docente.getEmail());
                        out.flush();
                        out.close();
                    } else {
                        boolean es = fadmail_DOCENTE(pr, id_docente, dataoggi, st,
                                docente.getNome().toUpperCase() + " " + docente.getCognome().toUpperCase(), datainvito,
                                docente.getEmail().toLowerCase());
                        if (es) {
                            out.print("success");
                        } else {
                            out.print("ERRORE INVIO MAIL A :" + docente.getEmail());
                        }
                        out.flush();
                        out.close();
                    }
                }
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
