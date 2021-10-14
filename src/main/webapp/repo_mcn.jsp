<%@page import="java.util.LinkedList"%>
<%@page import="it.refill.engine.DocumentiLezione"%>
<%@page import="it.refill.engine.DatiLezione"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="java.util.List"%>
<%@page import="it.refill.engine.GenericUser"%>
<%@page import="it.refill.engine.Action"%>
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
        <script src="js/jquery-3.4.1.js"></script>
        <script src="js/external_api.js"></script>
        <link rel="shortcut icon" href="favicon.ico" />
    </head>
    <body id="page-top" onload="return  document.getElementById('sidebarToggle').click();" >

        <!-- Page Wrapper -->
        <div id="wrapper">

            <!-- Sidebar -->
            <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

                <!-- Sidebar - Brand -->
                <a class="sidebar-brand d-flex align-items-center justify-content-center" href="repo_mcn.jsp" onclick="return false;">
                    <div class="sidebar-brand-icon rotate-n-15">
                        <i class="fas fa-laugh-wink"></i>
                    </div>
                    <div class="sidebar-brand-text mx-3">Conference <sup>3.0</sup></div>
                </a>

                <!-- Divider -->
                <hr class="sidebar-divider my-0">

                <!-- Nav Item - Dashboard -->
                <li class="nav-item" >
                    <a class="nav-link" href="repo_mcn.jsp" target="_blank" >
                        <i class="fas fa-fw fa-book-open"></i>
                        <span>Materiale Didattico</span></a>
                </li>
                <!-- Divider -->
                <hr class="sidebar-divider">
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
                            <h2 class="h4 mb-0 text-gray-800"><%=us_role%>: <small><%=us_nome%> <%=us_cognome%></small></h2>
                            <h2 class="h4 mb-0 text-gray-800">CF/Username: <small><%=us_cf%></small></h2>
                        </div>


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


    </body>
    <%}%>
</html>
