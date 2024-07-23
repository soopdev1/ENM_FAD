<%@page import="java.util.LinkedList"%>
<%@page import="rc.so.engine.DocumentiLezione"%>
<%@page import="rc.so.engine.DatiLezione"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.engine.GenericUser"%>
<%@page import="rc.so.engine.Action"%>
<!DOCTYPE html>
<html lang="en">
    <%
        if (Action.checkSession(session)) {
            response.sendRedirect("login_mcn.jsp");
        } else {
            String us_role = Action.getSessionValue(session, "us_role");
            String us_nome = Action.getSessionValue(session, "us_nome");
            String us_cognome = Action.getSessionValue(session, "us_cognome");
            String us_cf = Action.getSessionValue(session, "us_cf");
    %>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Materiale didattico YISU</title>
        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link href="css/googlefontcss.css" rel="stylesheet">
        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
        <link href="Bootstrap2024/assets/css/bootstrap-italia.min.css" rel="stylesheet">
        <script src="js/external_api.js"></script>
        <link rel="shortcut icon" href="favicon.ico" />
        <style>
            /* Additional CSS to ensure proper alignment and spacing */
            .content-wrapper {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }
            .main-content {
                flex: 1;
            }
            .footer {
                flex-shrink: 0;
            }
        </style>
    </head>
    <body id="page-top">
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
                            <a class="nav-link active " href="repo_mcn.jsp"><span>Materiale Didattico</span></a>
                        </li>
                         <li class="nav-item active">
                            <a class="nav-link  " href="fad_mcn.jsp"><span>Conferenza</span></a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Page Wrapper -->
        <div id="wrapper">

            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column content-wrapper">

                <!-- Main Content -->
                <div id="content" class="main-content">

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

                        <div class="row">
                            <!-- Area Chart -->
                            <div class="col-xl-12 col-lg-12">
                                <div class="list-group">
                                    <%
                                        LinkedList<DocumentiLezione> dl1 = Action.repository();
                                        
                                        for (int i = 0; i < dl1.size(); i++) {
                                            DocumentiLezione dl0 = dl1.get(i);
                                            if (dl0.getTipo().equals("LINK")) {%>
                                    <a href="<%=dl0.getPath()%>" target="_blank"
                                       class="list-group-item list-group-item-action"><%=dl0.getCodice_ud()%> - <i class="fa fa-link text-primary"></i> LINK <%=(i + 1)%> </a>
                                    <%} else {%>
                                    <form action="Download" target="_blank" method="POST" >
                                        <input type="hidden" name="path" value="<%=dl0.getPath()%>" />
                                        <button type="submit" id="pdf_<%=dl0.getId_docud()%>"
                                                class="list-group-item list-group-item-action">
                                            <%=dl0.getCodice_ud()%> - <i class="fa fa-file-pdf text-danger"></i> PDF <%=(i + 1)%> 
                                        </button>
                                    </form>
                                    <%}
                                        }%>

                                </div>   
                            </div>
                        </div>

                    </div>
                </div>
                <!-- End of Main Content -->

                <!-- Footer -->
                <!-- End of Footer -->

            </div>
            <!-- End of Content Wrapper -->

        </div>
        <%@include file="Bootstrap2024/index/login/Footer_login.jsp" %>

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


    </body>
    <%}%>
</html>
