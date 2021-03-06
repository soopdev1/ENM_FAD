/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.servlet;

import it.refill.engine.Action;
import static it.refill.engine.Action.SSOACTIVE;
import static it.refill.engine.Action.getNanoSecond;
import static it.refill.engine.Action.getRequestValue;
import static it.refill.engine.Action.getUserMC;
import static it.refill.engine.Action.getUserSA;
import static it.refill.engine.Action.log_ajax;
import static it.refill.engine.Action.redirect;
import static it.refill.engine.Action.verificaStanza_OGGI;
import it.refill.engine.GenericUser;
import it.refill.sso.ClientSSO;
import static it.refill.sso.ClientSSO.login;
import static it.refill.sso.ClientSSO.logout;
import it.refill.sso.ResponseSSO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

/**
 *
 * @author rcosco
 */
public class Login extends HttpServlet {

    protected void logout_mcn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log_ajax("L2",
                request.getSession().getAttribute("us_stanza").toString(),
                request.getSession().getAttribute("us_role").toString() + ":"
                + request.getSession().getAttribute("us_cod").toString(),
                getNanoSecond());

        request.getSession().setAttribute("us_cod", null);
        request.getSession().setAttribute("us_pro", null);
        request.getSession().setAttribute("us_nome", null);
        request.getSession().setAttribute("us_cognome", null);
        request.getSession().setAttribute("us_cf", null);
        request.getSession().setAttribute("us_stanza", null);
        request.getSession().setAttribute("us_role", null);

        //SSO
        if (SSOACTIVE) {
            logout(request.getSession().getAttribute("us_sso").toString());
            request.getSession().setAttribute("us_sso", null);
            request.getSession().setAttribute("us_actk", null);
            request.getSession().setAttribute("us_retk", null);
        }
        request.getSession().invalidate();
        request.getSession().setMaxInactiveInterval(1);
        redirect(request, response, "login_mcn.jsp");
    }

    protected void login_fad_mcn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nomestanza = deleteWhitespace(getRequestValue(request, "roomname"));
        String progetto = deleteWhitespace(getRequestValue(request, "progetto"));
        String username = deleteWhitespace(getRequestValue(request, "codfisc"));
        String view = deleteWhitespace(getRequestValue(request, "view"));

        boolean ok = verificaStanza_OGGI(progetto, nomestanza);

        if (ok) {
            switch (view) {
                case "2": {
                    //SA
                    GenericUser user = getUserSA(username);
                    if (user != null) {
                        HttpSession se = request.getSession();
                        se.setAttribute("us_cod", user.getIdallievi());
                        se.setAttribute("us_pro", progetto);
                        se.setAttribute("us_nome", capitalize(user.getNome().toLowerCase()));
                        se.setAttribute("us_cognome", capitalize(user.getCognome().toLowerCase()));
                        se.setAttribute("us_cf", user.getCodicefiscale().toUpperCase());
                        se.setAttribute("us_stanza", nomestanza.toUpperCase());
                        se.setAttribute("us_role", "SOGGETTO ATTUATORE");
                        log_ajax("L1", nomestanza.toUpperCase(), "SOGGETTO ATTUATORE: " + user.getIdallievi(), getNanoSecond());
                        redirect(request, response, "conference_mcn_2022.jsp");
                    } else {
                        redirect(request, response, "logerr.jsp");
                    }
                    break;
                }
                case "1": {
                    //MC
                    GenericUser user = getUserMC(username);
                    if (user != null) {
                        HttpSession se = request.getSession();
                        se.setAttribute("us_cod", user.getIdallievi());
                        se.setAttribute("us_pro", progetto);
                        se.setAttribute("us_nome", capitalize(user.getNome().toLowerCase()));
                        se.setAttribute("us_cognome", capitalize(user.getCognome().toLowerCase()));
                        se.setAttribute("us_cf", user.getCodicefiscale().toUpperCase());
                        se.setAttribute("us_stanza", nomestanza.toUpperCase());
                        se.setAttribute("us_role", "ADMINMC");
                        log_ajax("L1", nomestanza.toUpperCase(), "ADMINMC: " + user.getIdallievi(), getNanoSecond());
                        redirect(request, response, "conference_mcn_2022.jsp");
                    } else {
                        redirect(request, response, "logerr.jsp");
                    }
                    break;
                }
                default:
                    redirect(request, response, "logerr.jsp");
                    break;
            }
        } else {
            redirect(request, response, "logerr.jsp");
        }

    }

    protected void login_edubik(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = deleteWhitespace(getRequestValue(request, "username"));
        String password = deleteWhitespace(getRequestValue(request, "password"));
        ResponseSSO sso = login(username, password);
        if (!sso.getAccess_token().startsWith("ERROR")) {
            HttpSession se = request.getSession();
            se.setAttribute("us_sso", username);
            //se.setAttribute("us_actk", ClientSSO.encrypt(sso.getAccess_token()));
            String refreshtoken = ClientSSO.encrypt(sso.getRefresh_token());
            se.setAttribute("us_retk", refreshtoken);
            try ( PrintWriter pw = response.getWriter()) {
                pw.print(refreshtoken);
            }
        } else {
            try ( PrintWriter pw = response.getWriter()) {
                pw.print("ERROR:CREDENZIALI ERRATE.");
            }
        }

    }

    protected void login_mcnnuovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nomestanza = deleteWhitespace(getRequestValue(request, "nomestanza"));
        String username = deleteWhitespace(getRequestValue(request, "username"));
        String password = deleteWhitespace(getRequestValue(request, "password"));
        String passwordMD5 = DigestUtils.md5Hex(password);
        GenericUser user = Action.loginUser(nomestanza, username, passwordMD5);
        if (user != null) {

//            ResponseSSO sso = login(username, password);
//            if (!sso.getAccess_token().startsWith("ERROR")) {
            HttpSession se = request.getSession();
            se.setAttribute("us_cod", user.getIdallievi());
            se.setAttribute("us_pro", user.getIdpro());
            se.setAttribute("us_nome", capitalize(user.getNome().toLowerCase()));
            se.setAttribute("us_cognome", capitalize(user.getCognome().toLowerCase()));
            se.setAttribute("us_cf", user.getCodicefiscale().toUpperCase());
            se.setAttribute("us_stanza", nomestanza.toUpperCase());
            se.setAttribute("us_role", user.getTipo());

            //SSO
//                if (SSOACTIVE) {
//                    se.setAttribute("us_sso", username);
//                    se.setAttribute("us_actk", ClientSSO.encrypt(sso.getAccess_token()));
//                    se.setAttribute("us_retk", ClientSSO.encrypt(sso.getRefresh_token()));
//                }
            log_ajax("L1", nomestanza.toUpperCase(), user.getTipo() + ":" + user.getIdallievi(), getNanoSecond());
            log_ajax("L10", nomestanza.toUpperCase(), "LOGIN " + user.getIdallievi() + " CON CREDENZIALI -> " + username + " - " + password, getNanoSecond());

            redirect(request, response, "conference_mcn_2022.jsp");
//            } else {
//
//                log_ajax("ER1", nomestanza.toUpperCase(), "LOGIN FALLITO SSO CON CREDENZIALI -> " + username + " - " + password, getNanoSecond());
//                log_ajax("ER1", nomestanza.toUpperCase(), "LOGIN FALLITO SSO: " + sso.toString(), getNanoSecond());
//
//                redirect(request, response, "login_mcn.jsp?error=yes");
//            }
        } else {
            //
//            System.out.println("it.refill.servlet.Login.login_mcnnuovo()");
//            HttpSession se = request.getSession();
//            se.setAttribute("us_cod", "MCN");
//            se.setAttribute("us_nome", capitalize("MCN"));
//            se.setAttribute("us_cognome", capitalize("MCN"));
//            se.setAttribute("us_cf", "MCN");
//            se.setAttribute("us_stanza", "FADMCNDD_316_A1");
//            se.setAttribute("us_role", "ALLIEVO");
//            se.setAttribute("us_pro", "316");
//
//            //SSO
//            se.setAttribute("us_sso", username);
//            se.setAttribute("us_actk","dadsdasaddas");
//            se.setAttribute("us_retk", "dsaadsadsdsa");
//
//            redirect(request, response, "conference_mcn_2022.jsp");

            log_ajax("ER1", nomestanza.toUpperCase(), "LOGIN FALLITO CON CREDENZIALI -> " + username + " - " + password, getNanoSecond());
            redirect(request, response, "login_mcn.jsp?error=yes");
        }

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            String type = request.getParameter("type");
            switch (type) {
                case "login_mcnnuovo":
                    login_mcnnuovo(request, response);
                    break;
                case "login_fad_mcn":
                    login_fad_mcn(request, response);
                    break;
                case "login_edubik":
                    login_edubik(request, response);
                    break;
                case "logout_mcn":
                    logout_mcn(request, response);
                    break;
                default:
                    break;
            }
        } catch (ServletException | IOException ex) {
        }
    }

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
