<%@page import="rc.so.engine.DocumentiLezione"%>
<%@page import="rc.so.engine.DatiLezione"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.engine.GenericUser"%>
<%@page import="rc.so.engine.Action"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>

<!DOCTYPE html>
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
        <link href="Bootstrap2024/assets/css/bootstrap-italia.min.css" rel="stylesheet">
        <script src="https://meet.jit.si/external_api.js"></script>
        <script src="https://meet.jit.si/libs/lib-jitsi-meet.min.js"></script>
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
                userInfo: {
                    email: 'email@jitsiexamplemail.com',
                    displayName: name
                },
                configOverwrite: {
                    enableWelcomePage: false,
                    //raf 160721
                    enableForcedReload: false,
                    enableIceRestart: true,
                    // raf 14122021
                    startAudioMuted: 1000,
                    startVideoMuted: 1000,
                    startSilent: false,
                    maxFullResolutionparticipants: -1,
                    fileRecordingsEnabled: false,
                    useNewBandwithAllocationStrategy: true,
                    useTurnUdp: true,
                    enableEncodedTransformSupport: true,
                    defautlLocalDisplayName: 'Allievo non identificato',
                    defaultRemoteDisplayName: 'Allievo non identificato',
                    defaultLanguage: 'it',
                    disableProfile: true
                },
                interfaceConfigOverwrite: {
                    TOOLBAR_BUTTONS: but1
                }
            };

            var api = new JitsiMeetExternalAPI(domain, options);
            //api.executeCommand('displayName', name);
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
        <%@include file="Bootstrap2024/index/index_SoggettoAttuatore/Header_soggettoAttuatore.jsp" %>
        <nav class="navbar navbar-expand-lg has-megamenu" aria-label="Menu principale">
            <button type="button" aria-label="Mostra o nascondi il menu" class="custom-navbar-toggler" aria-controls="menu" aria-expanded="false" data-bs-toggle="navbarcollapsible" data-bs-target="#navbar-E">
                <span>
                    <svg role="img" class="icon"><use href="../../Bootstrap2024/assets/svg/sprites.svg#it-burger"></use></svg>
                </span>
            </button>
            <div class="navbar-collapsable" id="navbar-E">
                <div class="overlay fade"></div>
                <div class="close-div">
                    <button type="button" aria-label="Chiudi il menu" class="btn close-menu">
                        <span><svg role="img" class="icon"><use href="../../Bootstrap2024/assets/svg/sprites.svg#it-close-big"></use></svg></span>
                    </button>
                </div>
                <div class="menu-wrapper justify-content-lg-between">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link active " href="fad_mcn.jsp"><span>Conferenza</span></a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="repo_mcn.jsp"><span>Materiale Didattico</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#"  onclick="return logout();">
                                <i class="fas fa-sign-out-alt fa-sm text-white-50"></i> LOGOUT</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Page Wrapper -->
        <div id="wrapper">

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
                            <b>STANZA:</b> <%=us_stanza%>
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
                                    if (doc != null && !doc.isEmpty()) {
                                %>
                                <div class="form-group" >
                                    <h6 class="m-0 font-weight-bold text-primary">Seleziona Docente:</h6>
                                    <select class="js-example-basic-single" id="listadoc" style="width: 50%;">
                                        <%
                                            for (int x = 0; x < doc.size(); x++) {
                                                GenericUser gu = doc.get(x);
                                        %>
                                        <option value="<%=StringEscapeUtils.escapeHtml4(String.valueOf(gu.getIdallievi()))%>"><%=StringEscapeUtils.escapeHtml4(gu.getCognome()).toUpperCase()%> <%=StringEscapeUtils.escapeHtml4(gu.getNome()).toUpperCase()%> - <%=StringEscapeUtils.escapeHtml4(gu.getEmail()).toLowerCase()%></option>
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
                                        <option value="<%=StringEscapeUtils.escapeHtml4(String.valueOf(gu.getIdallievi()))%>"><%=StringEscapeUtils.escapeHtml4(gu.getCognome()).toUpperCase()%> <%=StringEscapeUtils.escapeHtml4(gu.getNome()).toUpperCase()%> - <%=StringEscapeUtils.escapeHtml4(gu.getEmail()).toLowerCase()%></option>
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
                                boolean ssotester = false;
                                String rto = "";
                                try {
                                    //System.out.println("className.methodName(1) "+Action.get_Path("id.pro.sso.tester"));
                                    //System.out.println("className.methodName(2) "+session.getAttribute("us_pro").toString());
                                    ssotester = Action.get_Path("id.pro.sso.tester").contains(Action.checkAttribute(session,"us_pro"));
                                    rto = Action.checkAttribute(session,"us_retk");
                                } catch (Exception e) {
                                    ssotester = false;
                                }
                                String linkedu = Action.get_Path("linkedu1");
                                if (ssotester && linkedu != null && (us_role.equals("ALLIEVO") || us_role.equals("DOCENTE"))) {%>
                            <div class="col-xl-12 col-lg-12">
                                <hr>
                                <form action="<%=StringEscapeUtils.escapeHtml4(linkedu)%>" method="POST" id="formedu_r" target="_blank">
                                    <input type="hidden" name="us_retk" id="RefreshToken_r" value="<%=rto%>"/>
                                </form>
                                <button class="btn btn-primary btn-lg btn-block" onclick="document.getElementById('formedu_r').submit()">VAI ALL'AREA LAVORO</button>
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
                                <b>GIORNO DI LEZIONE:</b> <%=StringEscapeUtils.escapeHtml4(dl1.getGIORNODILEZIONE())%>
                                <br>
                                <b>NUMERO LEZIONE:</b> <%=StringEscapeUtils.escapeHtml4(dl1.getNUMEROLEZIONE())%>
                                <br>
                                <b>MODULO:</b> <%=StringEscapeUtils.escapeHtml4(dl1.getUNITADIDATTICA())%>

                            </div>
                            <%if (!dl1.getFiles().isEmpty()) {%>
                            <div class="col-xl-3 col-lg-3">
                                <div class="list-group">
                                    <%
                                    int i = 0;
                                    for (DocumentiLezione dl0 : dl1.getFiles()) {
                                            if (dl0.getTipo().equals("LINK")) {%>
                                    <a href="<%=StringEscapeUtils.escapeHtml4(dl0.getPath())%>" target="_blank"
                                       class="list-group-item list-group-item-action"><i class="fa fa-link text-primary"></i> LINK <%=(i + 1)%> </a>
                                    <%
                                        } else {%>
                                    <form action="Download" target="_blank" method="POST" >
                                        <input type="hidden" name="path" value="<%=StringEscapeUtils.escapeHtml4(dl0.getPath())%>" />
                                        <button type="submit" id="pdf_<%=dl0.getId_docud()%>"
                                                class="list-group-item list-group-item-action">
                                            <i class="fa fa-file-pdf text-danger"></i> PDF <%=(i + 1)%> 
                                        </button>
                                    </form>
                                    <%}i++;
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
                                <h4 class="m-0 font-weight-bold text-primary"><a href="<%=StringEscapeUtils.escapeHtml4(q1) + "?ut=" + StringEscapeUtils.escapeHtml4(us_cod)%>" target="_blank">Clicca qui per rispondere al questionario</a></h4>
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
                <%@include file="Bootstrap2024/index/login/Footer_login.jsp" %>
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
