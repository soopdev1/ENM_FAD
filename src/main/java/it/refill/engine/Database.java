/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.engine;

import com.google.gson.Gson;
import static it.refill.engine.Action.conf;
import static it.refill.engine.Action.estraiEccezione;
import static it.refill.engine.Action.pat_5;
import static java.lang.Class.forName;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author rcosco
 */
public class Database {

    public Connection c;
//    public Logger log;

    public Database(Logger l) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String user = conf.getString("db.user");
        String password = conf.getString("db.pass");
        String host = conf.getString("db.host") + ":3306/" + conf.getString("db.name");
        try {
            forName(driver).newInstance();
            Properties p = new Properties();
            p.put("user", user);
            p.put("password", password);
            p.put("characterEncoding", "UTF-8");
            p.put("passwordCharacterEncoding", "UTF-8");
            p.put("useSSL", "false");
            p.put("connectTimeout", "1000");
            p.put("useUnicode", "true");
            this.c = DriverManager.getConnection("jdbc:mysql://" + host, p);
        } catch (Exception ex) {
            if (this.c != null) {
                try {
                    this.c.close();
                } catch (Exception ex1) {
                }
            }
            this.c = null;
        }
    }

    public Connection getC() {
        return c;
    }

    public void setC(Connection c) {
        this.c = c;
    }

    public void closeDB() {
        try {
            if (this.c != null) {
                this.c.close();
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
    }

    public boolean connesso(Connection con) {
        try {
            return con != null && !con.isClosed();
        } catch (Exception ignored) {
        }
        return false;
    }

    public void insertTR(String type, String user, String descr) {
        try {
            PreparedStatement ps = this.c.prepareStatement("INSERT INTO tracking (azione,iduser,timestamp) VALUES (?,?,?)");
            ps.setString(1, "FAD: " + descr);
            ps.setString(2, user);
            ps.setString(3, getNow());
            ps.execute();
        } catch (SQLException ex) {
        }
    }

    public String getNow() {
        try {
            String sql = "SELECT now()";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception ex) {
        }
        return new DateTime().toString(pat_5);
    }

    public List<String> cf_list(String table) {
        List<String> out = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT(codicefiscale) FROM " + table;
            try ( PreparedStatement ps = this.c.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser getDocente(String cf) {
        GenericUser out = null;
        try {
            String sql = "SELECT iddocenti, nome, cognome, codicefiscale FROM docenti WHERE codicefiscale = ?";
            try ( PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, cf);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out = new GenericUser(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), "NONE", null);
                    }
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser getUserSA(String username) {
        GenericUser out = null;
        try {
            String sql = "SELECT username,idsoggetti_attuatori FROM user WHERE username = ? AND tipo = ?";
            try ( PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, "1");
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int idsa = rs.getInt(2);
                        String sql1 = "SELECT ragionesociale FROM soggetti_attuatori WHERE idsoggetti_attuatori = ?";
                        try ( PreparedStatement ps1 = this.c.prepareStatement(sql1)) {
                            ps1.setInt(1, idsa);
                            try ( ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    out = new GenericUser(rs.getString(1), rs1.getString(1), "", rs.getString(1), "", null);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser getUserMC(String username) {
        GenericUser out = null;
        try {
            String sql = "SELECT username FROM user WHERE username = ? AND tipo IN (2,5)";
            try ( PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, username);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out = new GenericUser(rs.getString(1), "ADMIN", "MC", rs.getString(1), "", null);
                    }
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser getUser(int id) {
        GenericUser out = null;
        try {
            String sql = "SELECT username FROM user WHERE iduser = ?";
            try ( PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setInt(1, id);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out = new GenericUser(rs.getString(1), "ADMIN", "US", rs.getString(1), "", null);
                    }
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser getUser(String mail) {
        GenericUser out = null;
        try {
            String sql = "SELECT username FROM user WHERE email = ?";
            try ( PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, mail);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out = new GenericUser(rs.getString(1), "ADMIN", "US", rs.getString(1), "", null);
                    }
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser getAllievo(String cf) {
        GenericUser out = null;
        try {
            String sql = "SELECT idallievi, nome, cognome, codicefiscale, email , telefono FROM allievi WHERE codicefiscale = ?";
            try ( PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, cf);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out = new GenericUser(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                    }
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public String getNanoSecond() {
        try {
            String sql = "select current_timestamp(6)";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(new Date());
    }

    public List<GenericUser> get_UserProg(String idpr, boolean mail) {
        List<GenericUser> list = new ArrayList<>();
        try {

            String sql = "SELECT * FROM allievi WHERE idprogetti_formativi = '" + idpr + "' AND id_statopartecipazione = '01' ";
            if (mail) {
                sql += " AND email REGEXP '^[a-zA-Z0-9][a-zA-Z0-9._-]*[a-zA-Z0-9._-]@[a-zA-Z0-9][a-zA-Z0-9._-]*[a-zA-Z0-9]\\\\.[a-zA-Z]{2,63}$'";
            }

            sql += " ORDER BY cognome,nome";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(new GenericUser(rs.getString(1), rs.getString(4), rs.getString(5),
                            rs.getString(6), rs.getString(8), rs.getString("telefono")));
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return list;
    }

    public List<GenericUser> get_DocProg(String idpr, boolean mail) {
        List<GenericUser> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM docenti WHERE iddocenti in (SELECT iddocenti FROM progetti_docenti WHERE idprogetti_formativi='" + idpr + "')";
            if (mail) {
                sql += " AND email REGEXP '^[^@]+@[^@]+\\.[^@]{2,}$'";
            }
            sql += " ORDER BY cognome,nome";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(new GenericUser(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString("email"), null));
                }
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return list;
    }

    public String get_nomeProg(String idpr) {
        String out = "";
        try {
            String sql = "SELECT descrizione FROM progetti_formativi a WHERE idprogetti_formativi = '" + idpr + "'";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public String get_Stanza_conferenza_login(String id, String psw, String email) {
        String out = null;
        try {
            String sql = "SELECT nomestanza FROM fad_micro WHERE stato='0' AND idfad = '" + id
                    + "' AND password = '" + psw + "' AND partecipanti LIKE '%" + email + "%'";

            if (email == null) {
                sql = "SELECT nomestanza FROM fad_micro WHERE stato='0' AND idfad = '" + id
                        + "' AND password = '" + psw + "'";
            }

            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public String get_Stanza_CAD_login(String id, String psw, String email) {
        String out = null;
        try {
            String sql = "SELECT idcad FROM cad WHERE stato='0' AND idcad = '" + id + "' "
                    + "AND password = '" + psw + "' AND email = '" + email + "'";

            if (email == null) {
                sql = "SELECT idcad FROM cad WHERE stato='0' AND idcad = '" + id
                        + "' AND password = '" + psw + "'";
            }

            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = "CAD_" + rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public boolean verificaStanza_OGGI(String pr, String nomestanza) {
        boolean ok = false;
        try {
            String sql = "SELECT room,idprogetti_formativi FROM fad_access "
                    + "WHERE room  = '" + nomestanza + "' AND data=CURDATE() "
                    + "AND idprogetti_formativi = " + pr + "";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                ok = rs.next();
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }

        return ok;
    }

    public boolean verificaStanza(String pr, String nomestanza) {
        boolean ok = false;
        try {
            String sql = "SELECT nomestanza FROM fad_multi a WHERE stato='0' AND idprogetti_formativi = '" + pr + "' AND nomestanza = '" + nomestanza + "'";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                ok = rs.next();
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }

        return ok;
    }

    public String get_Stanza(String idpr) {
        String out = null;
        try {
            String sql = "SELECT nomestanza FROM fad a WHERE stato='0' AND idprogetti_formativi = '" + idpr + "'";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public void log_ajax(String type, String room, String action, String date) {
        try {
            String sql = "INSERT INTO fad_track (type,room,action,date) VALUES (?,?,?,?)";
            try ( PreparedStatement pst = this.c.prepareStatement(sql)) {
                pst.setString(1, type);
                pst.setString(2, room);
                pst.setString(3, action);
                pst.setString(4, date);
                pst.execute();
            }
        } catch (SQLException ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
    }

    public String get_Path(String id) {
        String out = null;
        try {
            String sql = "SELECT url FROM path WHERE id='" + id + "'";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public String[] get_Mail(String id) {
        String[] out = {"", ""};
        try {
            String sql = "SELECT oggetto,testo FROM email WHERE chiave='" + id + "'";
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out[0] = rs.getString(1);
                    out[1] = rs.getString(2);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public List<String> getMailFromConference(String nomestanza) {
        List<String> out = new ArrayList<>();
        try {
            String sql = "SELECT partecipanti FROM fad_micro WHERE idfad = " + nomestanza;
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = Arrays.asList(new Gson().fromJson(rs.getString(1), String[].class));
                }
            }
        } catch (SQLException ex) {
            out = new ArrayList<>();
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public String getPswFromConference(String nomestanza) {
        String out = null;
        try {
            String sql = "SELECT password FROM fad_micro WHERE idfad= " + nomestanza;
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public Fadroom getroom(String id) {
        Fadroom out = null;
        try {
            String sql = "SELECT * FROM fad_micro WHERE idfad=" + id;
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = new Fadroom(
                            rs.getInt("idfad"),
                            rs.getString("datacreazione"), rs.getString("nomestanza"),
                            rs.getString("stato"),
                            rs.getInt("iduser"),
                            rs.getString("partecipanti"),
                            rs.getString("password"),
                            rs.getString("fine"),
                            rs.getString("inizio"));
                }
            }
        } catch (SQLException ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser loginUser(String username, String password) {
        GenericUser out = null;
        try {
            String sql = "SELECT type,idsoggetto,idprogetti_formativi FROM fad_access WHERE user = ? AND psw = ?";
            try ( PreparedStatement ps1 = this.c.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);) {
                ps1.setString(1, username);
                ps1.setString(2, password);
                try ( ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next()) {
                        String idtype = rs1.getString(1);
                        int idsoggetto = rs1.getInt(2);
                        String idprogetti_formativi = rs1.getString(3);

                        String sql1;
                        if (idtype.equals("S")) {
                            sql1 = "SELECT idallievi, nome, cognome, codicefiscale, email, telefono FROM allievi WHERE idallievi = " + idsoggetto;
                        } else if (idtype.equals("D")) {
                            sql1 = "SELECT iddocenti, nome, cognome, codicefiscale, email FROM docenti WHERE iddocenti = " + idsoggetto;
                        } else if (idtype.equals("O")) {
                            sql1 = "SELECT id_staff, nome, cognome, telefono, email FROM staff_modelli "
                                    + "WHERE id_staff = " + idsoggetto;
                        } else {
                            return null;
                        }
                        try ( PreparedStatement ps2 = this.c.prepareStatement(sql1, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                if (idtype.equals("S")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6));
                                    out.setTipo(GenericUser.formatType(idtype));
                                    out.setIdpro(idprogetti_formativi);
                                } else if (idtype.equals("D")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), null);
                                    out.setTipo(GenericUser.formatType(idtype));
                                    out.setIdpro(idprogetti_formativi);
                                } else if (idtype.equals("O")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(4));
                                    out.setTipo(GenericUser.formatType(idtype));
                                    out.setIdpro(idprogetti_formativi);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser loginUser(String nomestanza, String username, String password) {
        GenericUser out = null;
        try {
            String sql = "SELECT type,idsoggetto,idprogetti_formativi FROM fad_access WHERE room = ? AND user = ? AND psw = ? AND data = curdate()";
            try ( PreparedStatement ps1 = this.c.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);) {
                ps1.setString(1, nomestanza);
                ps1.setString(2, username);
                ps1.setString(3, password);
                try ( ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next()) {
                        String idtype = rs1.getString(1);
                        int idsoggetto = rs1.getInt(2);
                        String idprogetti_formativi = rs1.getString(3);

                        String sql1;
                        if (idtype.equals("S")) {
                            sql1 = "SELECT idallievi, nome, cognome, codicefiscale, email, telefono FROM allievi WHERE idallievi = " + idsoggetto;
                        } else if (idtype.equals("D")) {
                            sql1 = "SELECT iddocenti, nome, cognome, codicefiscale, email FROM docenti WHERE iddocenti = " + idsoggetto;
                        } else if (idtype.equals("O")) {
                            sql1 = "SELECT id_staff, nome, cognome, telefono, email FROM staff_modelli "
                                    + "WHERE id_staff = " + idsoggetto;
                        } else {
                            return null;
                        }
                        try ( PreparedStatement ps2 = this.c.prepareStatement(sql1, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                if (idtype.equals("S")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6));
                                    out.setTipo(GenericUser.formatType(idtype));
                                    out.setIdpro(idprogetti_formativi);
                                } else if (idtype.equals("D")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), null);
                                    out.setTipo(GenericUser.formatType(idtype));
                                    out.setIdpro(idprogetti_formativi);
                                } else if (idtype.equals("O")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(4));
                                    out.setTipo(GenericUser.formatType(idtype));
                                    out.setIdpro(idprogetti_formativi);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public GenericUser loginUser(String idpro, String iduser, String data, String idtype, String username, String pass) {
        GenericUser out = null;
        try {
            String sql = "SELECT type FROM fad_access WHERE idprogetti_formativi = ? AND idsoggetto = ? AND data = ? AND type = ? AND user = ? AND psw = ?";
            try ( PreparedStatement ps1 = this.c.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);) {
                ps1.setInt(1, Integer.parseInt(idpro));
                ps1.setInt(2, Integer.parseInt(iduser));
                ps1.setString(3, data);
                ps1.setString(4, idtype);
                ps1.setString(5, username);
                ps1.setString(6, pass);
                try ( ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next()) {
                        String sql1;
                        if (idtype.equals("S")) {
                            sql1 = "SELECT idallievi, nome, cognome, codicefiscale, email, telefono FROM allievi WHERE idallievi = " + iduser;
                        } else if (idtype.equals("D")) {
                            sql1 = "SELECT iddocenti, nome, cognome, codicefiscale, email FROM docenti WHERE iddocenti = " + iduser;
                        } else {
                            return null;
                        }
                        try ( PreparedStatement ps2 = this.c.prepareStatement(sql1, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                if (idtype.equals("S")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6));
                                } else if (idtype.equals("D")) {
                                    out = new GenericUser(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), null);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;

    }

    public String showRoom(String idpro, String iduser, String data, String idtype) {
        String out = null;
        try {
            String sql = "SELECT room FROM fad_access WHERE idprogetti_formativi = ? AND idsoggetto = ? AND data = ? AND type = ?";
            try ( PreparedStatement ps1 = this.c.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);) {
                ps1.setInt(1, Integer.parseInt(idpro));
                ps1.setInt(2, Integer.parseInt(iduser));
                ps1.setString(3, data);
                ps1.setString(4, idtype);
                try ( ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next()) {
                        out = rs1.getString(1);
                    }
                }
            }
        } catch (Exception ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public String showUser(String idpro, String iduser, String idtype) {
        String out = null;
        try {
            String sql;
            if (idtype.equals("S")) {
                sql = "SELECT cognome,nome FROM allievi WHERE idprogetti_formativi = " + idpro + " AND idallievi = " + iduser;
            } else if (idtype.equals("D")) {
                sql = "SELECT cognome,nome FROM docenti WHERE iddocenti = " + iduser;
            } else {
                return out;
            }

            try ( PreparedStatement ps1 = this.c.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs1 = ps1.executeQuery();) {
                if (rs1.next()) {
                    out = rs1.getString(1).toUpperCase() + " " + rs1.getString(2).toUpperCase();
                }
            }
        } catch (Exception ex) {
            out = null;
            insertTR("E", "System", estraiEccezione(ex));
        }

        return out;
    }

    public LinkedList<DocumentiLezione> repository() {
        LinkedList<DocumentiLezione> out = new LinkedList<>();
        try {
            String sql2 = "SELECT * FROM documenti_unitadidattiche WHERE deleted = 0";
            try ( PreparedStatement ps2 = this.c.prepareStatement(sql2, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs2 = ps2.executeQuery();) {
                while (rs2.next()) {
                    out.add(new DocumentiLezione(rs2.getInt("id_docud"), rs2.getString("tipo"), rs2.getString("path"), rs2.getString("codice_ud")));
                }
            }
        } catch (Exception ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return out;
    }

    public List<DatiLezione> datilezione(String datasql, String idprogetto) {
        List<DatiLezione> list = new LinkedList<>();

        try {
            String sql = "SELECT lc.id_lezionecalendario AS NUMEROLEZIONE,"
                    + "lc.lezione AS GIORNODILEZIONE,"
                    + "mp.id_progettoformativo AS PROGETTO,"
                    + "lm.giorno AS DATALEZIONE,"
                    + "lm.orario_start AS INIZIOLEZIONE,"
                    + "lm.orario_end AS FINELEZIONE,"
                    + "lc.ore AS ORE,"
                    + "lc.codice_ud AS UNITADIDATTICA,"
                    + "ud.fase AS FASE"
                    + " FROM lezioni_modelli lm, modelli_progetti mp, lezione_calendario lc, unita_didattiche ud"
                    + " WHERE lm.giorno = '" + datasql + "' AND mp.id_progettoformativo=" + idprogetto
                    + " AND lm.id_modelli_progetto=mp.id_modello AND lc.id_lezionecalendario=lm.id_lezionecalendario AND lc.codice_ud = ud.codice";

            try ( PreparedStatement ps1 = this.c.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs1 = ps1.executeQuery();) {
                while (rs1.next()) {
                    DatiLezione out = new DatiLezione(
                            rs1.getString("NUMEROLEZIONE"),
                            rs1.getString("GIORNODILEZIONE"),
                            rs1.getString("PROGETTO"),
                            rs1.getString("DATALEZIONE"),
                            rs1.getString("INIZIOLEZIONE"),
                            rs1.getString("FINELEZIONE"),
                            rs1.getString("ORE"),
                            rs1.getString("UNITADIDATTICA"),
                            rs1.getString("FASE"));

                    String sql2 = "SELECT * FROM documenti_unitadidattiche WHERE codice_ud = '" + rs1.getString("UNITADIDATTICA") + "' AND deleted = 0";
                    try ( PreparedStatement ps2 = this.c.prepareStatement(sql2, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);  ResultSet rs2 = ps2.executeQuery();) {
                        LinkedList<DocumentiLezione> dl = new LinkedList<>();
                        while (rs2.next()) {
                            dl.add(new DocumentiLezione(rs2.getInt("id_docud"), rs2.getString("tipo"), rs2.getString("path"), rs2.getString("codice_ud")));
                        }
                        out.setFiles(dl);
                    }
                    list.add(out);
                }
            }
        } catch (Exception ex) {
            insertTR("E", "System", estraiEccezione(ex));
        }
        return list;
    }

}
