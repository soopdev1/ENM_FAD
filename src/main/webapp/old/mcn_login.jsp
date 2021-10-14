<%-- 
    Document   : mcn_login
    Created on : 8-mar-2021, 10.43.35
    Author     : rcosco
--%>

<%@page import="org.joda.time.DateTime"%>
<%@page import="it.refill.engine.Action"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="it">
    <head>
        <title>Login - MCN</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!--===============================================================================================-->	
        <link rel="icon" type="image/png" href="favicon.ico"/>
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
        <!--===============================================================================================-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
        <!--===============================================================================================-->	
        <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
        <!--===============================================================================================-->	
        <link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="css/util.css">
        <link rel="stylesheet" type="text/css" href="css/main.css">
        <!--===============================================================================================-->
    </head>

    <%
        String idpro = Action.getRequestValue(request, "idpro");
        String iduser = Action.getRequestValue(request, "iduser");
        String idmodulo = Action.getRequestValue(request, "idmodulo");
        String idtype = Action.getRequestValue(request, "idtype");
        DateTime today = new DateTime();
        String date1 = today.toString("dd/MM/yyyy");
        String date2 = today.toString("yyyy-MM-dd");

        idpro = "1";
        iduser = "1";
        idtype = "S";

        String utente = Action.showUser(idpro, iduser, idtype);

    %>
    <body>  
        <div class="limiter">
            <div class="container-login100">
                <%if (utente != null) {%>
                <div class="wrap-login100 p-b-160 p-t-50">
                    <form class="login100-form validate-form" autocomplete="off" action="Login" method="POST">

                        <input type="hidden" name="type" value="login_mcn_password"/>
                        <input type="hidden" name="idpro" value="<%=idpro%>"/>
                        <input type="hidden" name="iduser" value="<%=iduser%>"/>
                        <input type="hidden" name="idmodulo" value="<%=idmodulo%>"/>
                        <input type="hidden" name="idtype" value="<%=idtype%>"/>
                        <input type="hidden" name="date" value="<%=date2%>"/>

                        <div style="display:none">
                            <input type="password" tabindex="-1"/>
                        </div>
                        <span class="login100-form-title p-b-43">
                            LOGIN FAD <br>LEZIONE DEL: <%=date1%>
                            <br>UTENTE: <b><u><%=utente%></u></b>
                        </span>
                        <div class="wrap-input100 rs1 validate-input" data-validate = "USERNAME OBBLIGATORIO">
                            <input class="input100" type="text" name="username">
                            <span class="label-input100">USERNAME</span>
                        </div>
                        <div class="wrap-input100 rs2 validate-input" data-validate="PASSWORD OBBLIGATORIA">
                            <input class="input100" type="password" name="pass" autocomplete="false" readonly onfocus="this.removeAttribute('readonly');">
                            <span class="label-input100">PASSWORD</span>
                        </div>

                        <div class="container-login100-form-btn">
                            <button class="login100-form-btn" type="submit">
                                ACCEDI
                            </button>
                        </div>
                        <div class="container-md login100-form-title"><br>
                            <h6><i class="fa fa-info-circle"></i> SE NON SEI L'UTENTE INDICATO RICHIEDI NUOVAMENTE LE CREDENZIALI AL SOGGETTO ATTUATORE O ALL'ENTE NAZIONALE MICROCREDITO.</h6>
                        </div>
                    </form>
                </div>
                <%} else {%>
                <span class="login100-form-title p-b-0">
                    LOGIN FAD <br>LEZIONE DEL: <%=date1%>
                </span>
                <span class="login100-form-title p-b-43">
                    <br><i class="fa fa-exclamation-triangle fa-2x text-danger"></i> ATTENZIONE!<br> IMPOSSIBILE ACCEDERE.<br>CONTROLLARE IL PROPRIO INDIRIZZO EMAIL PER IL CORRETTO LINK DI ACCESSO CON LE RELATIVE CREDENZIALI.
                </span>
                <%}%>
            </div>
        </div>
        <!--===============================================================================================-->
        <script src="vendor/jquery/jquery-3.2.1.min.js"></script>
        <!--===============================================================================================-->
        <script src="vendor/animsition/js/animsition.min.js"></script>
        <!--===============================================================================================-->
        <script src="vendor/bootstrap/js/popper.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
        <!--===============================================================================================-->
        <script src="vendor/select2/select2.min.js"></script>
        <!--===============================================================================================-->
        <script src="vendor/daterangepicker/moment.min.js"></script>
        <script src="vendor/daterangepicker/daterangepicker.js"></script>
        <!--===============================================================================================-->
        <script src="vendor/countdowntime/countdowntime.js"></script>
        <!--===============================================================================================-->
        <script src="js/main.js"></script>

    </body>
</html>