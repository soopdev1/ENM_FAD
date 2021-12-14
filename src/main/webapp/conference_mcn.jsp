<!DOCTYPE html>
<%@page import="it.refill.engine.DocumentiLezione"%>
<%@page import="it.refill.engine.DatiLezione"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="java.util.List"%>
<%@page import="it.refill.engine.GenericUser"%>
<%@page import="it.refill.engine.Action"%>
<html lang="en">
    <%
        if (Action.checkSession(session)) {
            response.sendRedirect("login_mcn.jsp");
        } else {
            String us_cod = Action.getSessionValue(session, "us_cod");
            String us_pro = Action.getSessionValue(session, "us_pro");
            String us_role = Action.getSessionValue(session, "us_role");
            String us_nome = Action.getSessionValue(session, "us_nome");
            String us_cognome = Action.getSessionValue(session, "us_cognome");
            String us_cf = Action.getSessionValue(session, "us_cf");
            String us_stanza = Action.getSessionValue(session, "us_stanza");
    %>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Conference ROOM</title>
        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link href="css/googlefontcss.css" rel="stylesheet">
        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
        <script src="js/jquery-3.4.1.js"></script>
        <script src="js/external_api.js"></script>
        <script src="js/lib-jitsi-meet.min.js"></script>
        <link rel="shortcut icon" href="favicon.ico" />
    </head>
    <script>

        function log_ajax(type, room, action) {
            $.ajax({
                url: "Print",
                type: "POST",
                data: {"type": type, "room": room, "action": action}
            });
        }

        function login() {
            log_ajax('L1', '<%=us_stanza%>', '<%=us_role%>' + ';' + '<%=us_cod%>');
            document.getElementById('startbutton').click();
        }

        function start(roomname) {
            var name = "<%=us_nome%>" + " " + "<%=us_cognome%>";
            var domain = "<%=Action.getDomainFAD()%>";
            $('#content-jitsi').html("");
            var but1 = ['microphone', 'camera', 'fullscreen', 'hangup', 'chat', 'desktop'];
            if ('<%=us_role%>' !== 'ALLIEVO') {
                but1 = ['microphone', 'camera', 'fullscreen', 'hangup', 'chat', 'desktop', 'settings'];
            }

            var options = {
                roomName: roomname,
                noSSL: false,
                enableWelcomePage: false,
                parentNode: document.getElementById('content-jitsi'),
                configOverwrite: {
                    enableWelcomePage: false,
                    //raf 160721
                    enableForcedReload:false,
                    enableIceRestart:true
                },
                interfaceConfigOverwrite: {
                    TOOLBAR_BUTTONS: but1
                }
            };
            var api = new JitsiMeetExternalAPI(domain, options);
            api.executeCommand('displayName', name);

            api.addEventListener('avatarChanged', function (OUT) {
                if (OUT.id === 'local') {
                    log_ajax('IN', '<%=us_stanza%>', "AVATAR MODIFICATO/INGRESSO -> " + '<%=us_cod%>');
                } else {
                    log_ajax('IN', '<%=us_stanza%>', "AVATAR MODIFICATO/INGRESSO -> " + OUT.id);
                }
            });

            api.addEventListener('videoConferenceJoined', function (OUT) {
                log_ajax('IN', '<%=us_stanza%>', "UTENTE LOGGATO CON ID " + OUT.id + " -- " + name);
                log_ajax('IN', '<%=us_stanza%>', "PARTECIPANTI -> " + api.getNumberOfParticipants());
            });

            api.addEventListener('videoConferenceLeft', function (OUT) {
                log_ajax('L3', '<%=us_stanza%>', '<%=us_role%>' + ';' + '<%=us_cod%>');
            });

            api.addEventListener('dominantSpeakerChanged', function (OUT) {
                log_ajax('IN', '<%=us_stanza%>', ("ORATORE MODIFICATO -> " + OUT.id));
            });

            api.addEventListener('outgoingMessage', function (OUT) {
                log_ajax('IN', '<%=us_stanza%>', "MESSAGGIO -> " + '<%=us_cod%> ' + OUT.message);
            });

            api.addEventListener('incomingMessage', function (OUT) {
                log_ajax('IN', '<%=us_stanza%>', "MESSAGGIO -> " + OUT.from + " -- " + OUT.nick + " -- " + OUT.message);
            });

            api.addEventListener('displayNameChange', function (OUT) {
                log_ajax('IN', '<%=us_stanza%>', "NOME CAMBIATO -> " + '<%=us_cod%> ' + OUT.id);
            });
            api.addEventListener('participantJoined', function (OUT) {
                log_ajax('IN', '<%=us_stanza%>', "NUOVO PARTECIPANTE -> " + OUT.id + " -- " + OUT.displayName);
            });
            api.addEventListener('participantLeft', function (OUT) {
                log_ajax('L4', '<%=us_stanza%>', "USCITA PARTECIPANTE -> " + OUT.id);
            });
            api.addEventListener('readyToClose', function (OUT) {
                log_ajax('L5', '<%=us_stanza%>', "USCITI TUTTI");
                api.dispose();
            });
        }

        function logout() {
            log_ajax('L2', '<%=us_stanza%>', '<%=us_role%>' + ';' + '<%=us_cod%>');
            $.ajax({
                type: "POST",
                url: "Login?type=logout_mcn"
            });
            window.location.href = "login_mcn.jsp";
        }

        function loadingpage() {
            log_ajax('L1', '<%=us_stanza%>', '<%=us_role%>' + ';' + '<%=us_cod%>');
            document.getElementById('startbutton').click();
            document.getElementById('sidebarToggle').click();
        }

    </script>
    <style type="text/css">
        #content-jitsi {
            bottom: 0;
            right: 0;
            width: 100%;
            height: 100vh;
            overflow: hidden;
        }
    </style>
    <body id="page-top" onload="return loadingpage();" >

        <!-- Page Wrapper -->
        <div id="wrapper">

            <!-- Sidebar -->
            <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

                <!-- Sidebar - Brand -->
                <a class="sidebar-brand d-flex align-items-center justify-content-center" href="conference_mcn.jsp" onclick="return false;">
                    <div class="sidebar-brand-icon rotate-n-15">
                        <i class="fas fa-laugh-wink"></i>
                    </div>
                    <div class="sidebar-brand-text mx-3">Conference <sup>3.0</sup></div>
                </a>

                <!-- Divider -->
                <hr class="sidebar-divider my-0">

                <!-- Nav Item - Dashboard -->
                <li class="nav-item active" >
                    <a class="nav-link" href="conference_mcn.jsp"  onclick="return false;">
                        <i class="fas fa-fw fa-tachometer-alt"></i>
                        <span>Stanza Principale</span></a>
                </li>
                <%if (true) {%>
                <li class="nav-item" >
                    <a class="nav-link" href="repo_mcn.jsp" target="_blank" >
                        <i class="fas fa-fw fa-book-open"></i>
                        <span>Materiale Didattico</span></a>
                </li>
                <%}%>
                <!-- Divider -->
                <hr class="sidebar-divider">

                <li class="nav-item">
                    <a class="nav-link" href="#"  onclick="return logout();">
                        <i class="fas fa-sign-out-alt fa-sm text-white-50"></i> LOGOUT</a>
                </li>

                <div style="display: none;">
                    <button id="sidebarToggle"></button>
                </div>

            </ul>
            <!-- End of Sidebar -->

            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column">

                <!-- Main Content -->
                <div id="content">

                    <!-- Topbar -->
                    <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                        <!-- Sidebar Toggle (Topbar) -->
                        <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                            <i class="fa fa-bars"></i>
                        </button>
                        <!-- Topbar Search -->
                        <div class="d-none d-sm-inline-block form-inline mr-auto ml-md-6 my-4 my-md-0 mw-100 navbar-search">
                            <b><%=us_role%>:</b> <%=us_nome%> <%=us_cognome%>
                        </div>
                        <div class="topbar-divider d-none d-sm-block"></div>
                        <div class="d-none d-sm-inline-block form-inline mr-auto ml-md-6 my-4 my-md-0 mw-100 navbar-search">   
                            <b>CF/Username:</b> <%=us_cf%>
                        </div>
                    </nav>
                    <!-- End of Topbar -->
                    <!-- Begin Page Content -->
                    <div class="container-fluid">
                        <!-- Page Heading -->
                        <div class="d-sm-flex align-items-center justify-content-between">
                            <h2 class="h4 mb-0 text-gray-800">STANZA: <small><%=us_stanza%></small></h2>
                            <h2 class="h4 mb-0 text-gray-800"><%=us_role%>: <small><%=us_nome%> <%=us_cognome%></small></h2>
                            <h2 class="h4 mb-0 text-gray-800">CF/Username: <small><%=us_cf%></small></h2>
                        </div>
                        <div class="row">
                            <button id="mailok" type="button" class="btn btn-primary modal fade" data-toggle="modal" data-target="#mailokModal">
                                Launch demo modal
                            </button>
                            <div class="modal fade" id="mailokModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-lg" role="document">
                                    <div class="modal-content">
                                        <div class="modal-body">
                                            <div class="card mb-4 py-3 border-left-success">
                                                <div class="card-body">
                                                    <i class="fa fa-check-circle text-success"></i> MAIL INVIATA CON SUCCESSO!
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-danger" data-dismiss="modal">Chiudi</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <button id="mailko" type="button" class="btn btn-primary modal fade" data-toggle="modal" data-target="#mailkoModal">
                                Launch demo modal
                            </button>
                            <div class="modal fade" id="mailkoModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-lg" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title text-danger">IMPOSSIBILE INVIARE MAIL.</h5>
                                        </div>
                                        <div class="modal-body">
                                            <div class="card mb-4 py-3 border-left-danger">
                                                <div class="card-body">
                                                    <i class="fa fa-exclamation-triangle text-danger"></i> ERRORE: <span id="errormesg"></span>
                                                </div>
                                            </div>

                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-danger" data-dismiss="modal">Chiudi</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <!-- Area Chart -->
                            <div class="col-xl-12 col-lg-12">
                                <!-- Card Header - Dropdown -->
                                <div id="content-jitsi">
                                    <a class="btn btn-primary" type="button" id="startbutton" href="#intro"  onclick="return start('<%=us_stanza%>');" >
                                        <i class="fa fa-video-camera"></i> AVVIA CONFERENCE</a></li> 
                                </div>
                            </div>

                            <%
                                DateTime today = new DateTime();
                                String date1 = today.toString("dd/MM/yyyy");
                                String date2 = today.toString("yyyy-MM-dd");

                                if (!us_role.equals("") && !us_role.equals("ALLIEVO")) {%>
                            <div class="col-xl-12 col-lg-12">
                                <hr>
                                <%    //if (!us_role.equals("") && !us_role.equals("ALLIEVO")) {
                                    List<GenericUser> usr = Action.get_UserProgMAILOK(us_pro);
                                    List<GenericUser> doc = Action.get_DocProgMAILOK(us_pro);
                                    if (!doc.isEmpty()) {
                                %>
                                <div class="form-group" >
                                    <h6 class="m-0 font-weight-bold text-primary">Seleziona Docente:</h6>
                                    <select class="js-example-basic-single" id="listadoc" style="width: 50%;">
                                        <%
                                            for (int x = 0; x < doc.size(); x++) {
                                                GenericUser gu = doc.get(x);
                                        %>
                                        <option value="<%=gu.getIdallievi()%>"><%=gu.getCognome().toUpperCase()%> <%=gu.getNome().toUpperCase()%> - <%=gu.getEmail().toLowerCase()%></option>
                                        <%}%>
                                    </select>
                                    <button class="btn btn-primary btn-icon-split"
                                            onclick="return send_DOC('listadoc');">
                                        <span class="icon text-white-50">
                                            <i class="fas fa-envelope"></i>
                                        </span>
                                        <span class="text">INVIA COMUNICAZIONE A DOCENTE</span>
                                    </button>
                                </div>
                                <%} else {%>
                                <div class="px-3 py-5 bg-gradient-secondary text-white"><b><u>ATTENZIONE!</u></b> Impossibile inviare comunicazione ai docenti in quanto nessuno di essi ha un indirizzo email correttamente configurato. Contattare il supporto.</div>
                                            <%}%>
                                <hr>
                                <%if (!usr.isEmpty()) {%>
                                <div class="form-group" >
                                    <h6 class="m-0 font-weight-bold text-primary">Seleziona Allievo:</h6>
                                    <select class="js-example-basic-single" id="listallievi" style="width: 50%;" onchange="return checksend('listallievi');">
                                        <option value="---">Tutti</option>
                                        <%
                                            for (int x = 0; x < usr.size(); x++) {
                                                GenericUser gu = usr.get(x);
                                        %>
                                        <option value="<%=gu.getIdallievi()%>"><%=gu.getCognome().toUpperCase()%> <%=gu.getNome().toUpperCase()%> - <%=gu.getEmail().toLowerCase()%></option>
                                        <%}%>
                                    </select>
                                    <button class="btn btn-info btn-icon-split"
                                            onclick="return send('listallievi');">
                                        <span class="icon text-white-50">
                                            <i class="fas fa-envelope"></i>
                                        </span>
                                        <span class="text" id="textsend">INVIA REMIND A TUTTI</span>
                                    </button>
                                </div>
                                <%} else {%>
                                <div class="px-3 py-5 bg-gradient-secondary text-white"><b><u>ATTENZIONE!</u></b> Impossibile inviare 
                                    comunicazione agli allievi in quanto nessuno di essi ha un indirizzo email correttamente configurato. 
                                    Contattare il supporto.</div>
                                    <%}%>
                                <script>

                                    function checksend(selectopt) {
                                        var select = $("#" + selectopt).val();
                                        if (select === "---") {
                                            $("#textsend").html(" INVIA REMIND A TUTTI");
                                        } else {
                                            $("#textsend").html(" INVIA REMIND");
                                        }
                                    }

                                    function send_DOC(selectopt) {
                                        if (selectopt === undefined) {
                                        } else {
                                            var select = $("#" + selectopt).val();
                                            var progetto = '<%=us_pro%>';
                                            var stanza = '<%=us_stanza%>';
                                            var datainvito = '<%=date1%>';
                                            var dataoggi = '<%=date2%>';
                                            $.ajax({

                                                url: "Mail_Docenti",
                                                type: 'POST',
                                                data: {
                                                    iduser: select,
                                                    pr: progetto,
                                                    tipo: 'D',
                                                    datainvito: datainvito,
                                                    dataoggi: dataoggi,
                                                    st: stanza
                                                },
                                                beforeSend: function () {
                                                    console.log("INVIO IN CORSO");
                                                },
                                                error: function (data, status, error) {
                                                    $("#errormesg").html(data.responseText);
                                                    $('#mailko').click();
                                                },
                                                success: function (data) {
                                                    if (data === "success") {
                                                        $('#mailok').click();
                                                    } else {
                                                        $("#errormesg").html(data);
                                                        $('#mailko').click();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    function send(selectopt) {
                                        if (selectopt === undefined) {
                                        } else {
                                            var select = $("#" + selectopt).val();
                                            var progetto = '<%=us_pro%>';
                                            var datainvito = '<%=date1%>';
                                            var dataoggi = '<%=date2%>';
                                            var stanza = '<%=us_stanza%>';
                                            $.ajax({
                                                url: "Mail",
                                                type: 'POST',
                                                data: {
                                                    iduser: select,
                                                    pr: progetto,
                                                    tipo: 'S',
                                                    datainvito: datainvito,
                                                    dataoggi: dataoggi,
                                                    st: stanza
                                                },
                                                beforeSend: function () {
                                                    console.log("INVIO IN CORSO");
                                                },
                                                error: function (data, status, error) {
                                                    $("#errormesg").html(data.responseText);
                                                    $('#mailko').click();
                                                },
                                                success: function (data) {
                                                    if (data === "success") {
                                                        $('#mailok').click();
                                                    } else {
                                                        $("#errormesg").html(data);
                                                        $('#mailko').click();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                </script>
                                <hr>
                            </div>
                            <%}%>
                            <%
                                List<DatiLezione> datilezione = Action.datilezione(date2, us_pro);
                                if (datilezione.size() > 0) {

                                    int lezionenum = 0;
                                    for (int x = 0; x < datilezione.size(); x++) {
                                        DatiLezione dl1 = datilezione.get(x);
                                        lezionenum = Action.parseINT(dl1.getNUMEROLEZIONE());
                            %>
                            <div class="col-xl-12 col-lg-12">
                                <hr>
                            </div>
                            <div class="col-xl-12 col-lg-12">
                                <h3 class="m-0 font-weight-bold text-primary">DETTAGLI LEZIONE</h3>
                            </div>

                            <div class="col-xl-3 col-lg-3">

                                <b>DATA:</b> <%=date1%> 
                                <br>
                                <b>GIORNO DI LEZIONE:</b> <%=dl1.getGIORNODILEZIONE()%>
                                <br>
                                <b>NUMERO LEZIONE:</b> <%=dl1.getNUMEROLEZIONE()%>
                                <br>
                                <b>MODULO:</b> <%=dl1.getUNITADIDATTICA()%>

                            </div>
                            <%if (dl1.getFiles().size() > 0) {%>
                            <div class="col-xl-3 col-lg-3">
                                <div class="list-group">
                                    <%for (int i = 0; i < dl1.getFiles().size(); i++) {
                                            DocumentiLezione dl0 = dl1.getFiles().get(i);
                                            if (dl0.getTipo().equals("LINK")) {%>
                                    <a href="<%=dl0.getPath()%>" target="_blank"
                                       class="list-group-item list-group-item-action"><i class="fa fa-link text-primary"></i> LINK <%=(i + 1)%> </a>
                                    <%} else {%>
                                    <form action="Download" target="_blank" method="POST" >
                                        <input type="hidden" name="path" value="<%=dl0.getPath()%>" />
                                        <button type="submit" id="pdf_<%=dl0.getId_docud()%>"
                                                class="list-group-item list-group-item-action">
                                            <i class="fa fa-file-pdf text-danger"></i> PDF <%=(i + 1)%> 
                                        </button>
                                    </form>
                                    <%}
                                        }%>

                                </div>    
                            </div>
                            <%} else {%>
                            <div class="col-xl-3 col-lg-3">
                                NON CI SONO DOCUMENTI RELATIVI A QUESTA LEZIONE
                            </div>
                            <%}%>
                            <div class="col-xl-12 col-lg-12">
                                <hr>
                            </div>

                            <%

                                if (us_role.contains("ALLIEVO")) {

                                    String q1 = Action.get_Path("questionario1");

                            %>
                            <div class="col-xl-6 col-lg-6">
                                <h3 class="m-0 font-weight-bold text-primary">QUESTIONARIO</h3>
                            </div>
                            <%if (q1 != null && (lezionenum > 0 && lezionenum < 6)) {%>
                            <div class="col-xl-6 col-lg-6">
                                <h4 class="m-0 font-weight-bold text-primary"><a href="<%=q1 + "?ut=" + us_cod%>" target="_blank">Clicca qui per rispondere al questionario</a></h4>
                            </div>
                            <%} else {
                                //QUESTIONARIO 2
                                //String q2 = Action.get_Path("questionario2");
                            %>

                            <%}
                                        }
                                    }
                                }%>
                        </div>
                    </div>
                </div>
                <footer class="sticky-footer bg-white">
                    <div class="container my-auto">
                        <div class="copyright text-center my-auto">
                            <span>YISU &copy; 2021</span>
                        </div>
                    </div>
                </footer>
                <!-- End of Footer -->

            </div>
            <!-- End of Content Wrapper -->

        </div>
        <!-- End of Page Wrapper -->

        <!-- Scroll to Top Button-->
        <a class="scroll-to-top rounded" href="#page-top">
            <i class="fas fa-angle-up"></i>
        </a>

        <!-- Logout Modal-->
        <!-- Bootstrap core JavaScript-->
        <script src="vendor/jquery/jquery.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

        <!-- Custom scripts for all pages-->
        <script src="js/sb-admin-2.min.js"></script>

        <link href="js/select2.min.css" rel="stylesheet" />
        <script src="js/select2.min.js"></script>

        <script>
            $(document).ready(function () {
                $('.js-example-basic-single').select2({
                    placeholder: "...",
                    theme: 'classic'
                });
            });
        </script>



    </body>
    <%}%>
</html>
