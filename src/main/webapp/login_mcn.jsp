<%@page import="org.joda.time.DateTime"%>
<%@page import="rc.so.engine.Action"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Login - MCN</title>
        <link href="vendor/fontawesome-6.5.2/css/all.min.css" rel="stylesheet" type="text/css">
        <link href="css/googlefontcss.css" rel="stylesheet">
        <link href="Bootstrap2024/assets/css/bootstrap-italia.min.css" rel="stylesheet">
        <link rel="shortcut icon" href="favicon.ico" />    
        <%
            String date1 = new DateTime().toString("dd/MM/yyyy");
            String linkedu = Action.get_Path("linkedu1");
            String error = Action.getRequestValue(request, "error");
        %>
        <script type="text/javascript">
            function checkerror() {
                var er1 = '<%=StringEscapeUtils.escapeHtml4(error)%>';
                if (er1 === "yes") {
                    document.getElementById('modalerrorbutton').click();
                }
            }
        </script>

    </head>
    <body onload="return checkerror();">
        <%@include file="Bootstrap2024/index/index_SoggettoAttuatore/Header_soggettoAttuatore.jsp" %>
        <div class="container login-container">
            <!-- Button trigger modal -->
            <div class="modal fade" tabindex="-1" aria-hidden="true">
                <button id="modalerrorbutton" type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
                </button>
            </div>
            <!-- Modal -->
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-danger" id="exampleModalLabel">ERRORE</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            I DATI DI ACCESSO INSERITI NON SONO CORRETTI. CONTROLLARE LA MAIL RICEVUTA.
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Outer Row -->
            <div class="row justify-content-center login-card">

                <div class="col-xl-10 col-lg-12 col-md-9">

                    <div class="container my-5">
                        <div class="row justify-content-center">
                            <div class="col-lg-8">
                                <div class="card o-hidden border-0 shadow-lg">
                                    <div class="card-body">
                                        <!-- Nested Row within Card Body -->
                                        <div class="row justify-content-center">
                                            <div class="col-lg-10">
                                                <div class="p-5">
                                                    <div class="text-center">
                                                        <h1 class="h4 text-gray-900 mb-4">Portale FAD <br> YES I START UP <%=Action.descr%></h1>
                                                            <%if (Action.test) {%>
                                                        <span class="bold text-center text-primary center-block">
                                                            <img src="images/beta.png" alt="" height="100"/>
                                                        </span>
                                                        <%}%>
                                                        <hr>
                                                        <h2 class="h4 text-gray-900 mb-4">LEZIONE DEL: <%=date1%></h2>
                                                    </div>
                                                    <hr>
                                                    <form class="login100-form validate-form" method="post" action="Login?type=login_mcnnuovo">
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-home"></i></span>
                                                            </div>
                                                            <input type="text" name="nomestanza" class="form-control required" placeholder="NOME STANZA" aria-label="NOMESTANZA" aria-describedby="basic-addon1" required/>
                                                        </div>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-user"></i></span>
                                                            </div>
                                                            <input type="text" name="username" class="form-control required" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1" required/>
                                                        </div>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-key"></i></span>
                                                            </div>
                                                            <input type="password" name="password" class="form-control required" placeholder="Password" aria-label="Password" aria-describedby="basic-addon1" required />
                                                        </div>
                                                        <small id="emailHelp" class="form-text text-muted">N.B. Inserire i dati di accesso ricevuti via mail in data odierna.</small>
                                                        <div class="input-group mb-3">
                                                            <button class="btn btn-primary btn-user btn-block">
                                                                LOGIN
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <%if (Action.SSOACTIVE) {%>
                                <div class="card o-hidden border-0 shadow-lg">
                                    <div class="card-body">
                                        <form action="" method="POST">
                                            <small class="form-text text-muted">AREA LAVORO</small>
                                            <small class="form-text text-muted">E' possibile accedere in qualsiasi momento alla piattaforma dedicata all'area lavoro inserendo l'ultima coppia di credenziali ricevuta sulla propria email.</small>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-user"></i></span>
                                                </div>
                                                <input type="text" id="username" name="us1" class="form-control required" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1" required/>
                                            </div>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-key"></i></span>
                                                </div>
                                                <input type="password" id="password" name="ps1"  class="form-control required" placeholder="Password" aria-label="Password" aria-describedby="basic-addon1" required />
                                            </div>
                                            <div class="input-group mb-3">
                                                <button class="btn btn-dark btn-user btn-block" onclick="return sendedubik();" type="button">
                                                    LOGIN AREA LAVORO
                                                </button>
                                            </div>
                                        </form>
                                        <form action="<%=StringEscapeUtils.escapeHtml4(linkedu)%>" method="POST" id="formedu_r">
                                            <input type="hidden" name="us_retk" id="RefreshToken_r" value="" />
                                        </form>
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="Bootstrap2024/index/login/Footer_login.jsp" %>


        <!-- Bootstrap core JavaScript-->
        <script src="vendor/jquery/jquery.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

        <!-- Custom scripts for all pages-->
        <script src="js/sb-admin-2.min.js"></script>
        <script type="text/javascript">
                                                        function sendedubik() {
                                                            var username = $("#username").val();
                                                            var password = $("#password").val();
                                                            $.ajax({
                                                                url: "Login",
                                                                async: false,
                                                                type: "POST",
                                                                crossDomain: true,
                                                                data: {"type": "login_edubik", "username": username, "password": password},
                                                                success: function (data, status, xhr) {   // success callback function
                                                                    if (data === null || data.startsWith("ERROR")) {
                                                                        document.getElementById("RefreshToken_r").value = "";
                                                                        document.getElementById('modalerrorbutton').click();
                                                                    } else {
                                                                        document.getElementById("RefreshToken_r").value = data;
                                                                        document.getElementById('formedu_r').submit();
                                                                    }
                                                                },
                                                                error: function (jqXhr, textStatus, errorMessage) { // error callback 
                                                                    console.error("Error: " + jqXhr);
                                                                    console.error("Error: " + textStatus);
                                                                    console.error("Error: " + errorMessage);
                                                                }
                                                            });
                                                        }
        </script>
    </body>
</html>
